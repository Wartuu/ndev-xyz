package impl;

import com.google.gson.JsonObject;
import impl.database.Account;
import impl.json.ConfigJson;
import impl.utils.finals.Global;
import impl.utils.irc.Room;
import impl.utils.irc.User;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;

public class WebsocketService extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    private static final List<Room> ROOMS = new ArrayList<>();
    private final Room hub;
    public boolean running = false;

    private final ConfigJson config;

    public WebsocketService(ConfigJson cfg) {
        super(new InetSocketAddress(cfg.getWebsocketPort()));
        this.config = cfg;
        this.hub = new Room("main", true);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("new connection from: " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());

        List<HttpCookie> cookies = HttpCookie.parse(clientHandshake.getFieldValue("Cookie"));
        Account account = null;



        boolean inCustomChannel = false;

        for (var cookie : cookies) {
            if(cookie.getName().equalsIgnoreCase("session-token")) {
                account = Global.database.getAccountBySession(cookie.getValue());
            }
        }


        if(account == null){
            webSocket.close();
            return;
        }

        for(var room : ROOMS) {
            if(room.containsAccount(account)) {
                inCustomChannel = true;
                room.updateUserWebsocket(account, webSocket);
            }
        }


        if(!inCustomChannel) {
            for (var user : hub.users) {
                if(user.account.getAuthToken().equals(account.getAuthToken())) {
                    hub.addUser(user);
                }
            }
        }





        for (var room : ROOMS) {
            if(room.containsAccount(account)) {
                room.updateUserWebsocket(account, webSocket);
                break;
            }
        }

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        logger.info(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected, code:" + code);
    }

    @Override
    public void onMessage(WebSocket webSocket, String msg) {
        logger.info(webSocket.getProtocol().getProvidedProtocol());
        Global.pluginManager.triggerHook("@onWebsocketMessage");

        if(msg.length() > 1000) {return;}

        logger.info(msg);
        JsonObject messageJson = Global.jsonParser.parse(msg).getAsJsonObject();


        User user = new User(Global.database.getAccountBySession(messageJson.get("session").getAsString()), webSocket);

        switch (messageJson.get("type").getAsString().toLowerCase(Locale.ROOT)) {
            case "create":
                String uuid = UUID.randomUUID().toString();
                ROOMS.add(new Room(uuid, user, false));
                user.webSocket.send(uuid);
                logger.info("created room");
                logger.info(uuid);
                break;
            case "join":
                String roomID = messageJson.get("room-id").getAsString();
                for (var room : ROOMS) {
                    if(room.ID.equals(roomID)) {
                        room.addUser(user);
                    }
                }
                break;
            case "message":
                boolean inCustomChannel = false;
                if(user.account != null) {
                    if(user.account.getChatAccess()) {
                        for (var room : ROOMS) {
                            if(room.containsAccount(user.account)) {
                                inCustomChannel = true;
                                room.broadcast(msg);
                                break;
                            }
                        }
                        if(!inCustomChannel) {
                            hub.broadcast(msg);

                        }
                    }
                }
                break;
            case "command":
                List<String> command = Arrays.stream(messageJson.get("message").getAsString().split(" ")).toList();

                break;

            default:
                logger.info("wrong type! passing to room (if connected to one): " + messageJson.get("type").getAsString());

                if(user.account != null) {
                    if(user.account.getChatAccess()) {
                        for (var room : ROOMS) {
                            if(room.containsAccount(user.account)) {
                                room.broadcast(msg);
                                break;
                            }
                        }
                    }
                }

                break;
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer msg) {
        Global.pluginManager.triggerHook("@onWebsocketMessage");
        broadcast(msg.array());
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        Global.pluginManager.triggerHook("@onWebsocketError");
        logger.error("error has occurred: " + e.toString());
    }

    @Override
    public void onStart() {
        logger.info("websocket service running at 127.0.0.1:" + this.getPort() + "/");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

        this.running = true;
    }
}
