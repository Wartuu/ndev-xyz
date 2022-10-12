package impl;

import com.google.gson.JsonObject;
import impl.database.Account;
import impl.database.Database;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import impl.utils.irc.Room;
import impl.utils.irc.User;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class WebsocketService extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    private static final List<Room> ROOMS = new ArrayList<>();
    private final Room hub;
    public boolean running = false;

    private final ConfigJson config;

    public WebsocketService(ConfigJson cfg) {
        super(new InetSocketAddress(cfg.getWebsocketPort()));
        this.config = cfg;
        this.hub = new Room("main");

    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("new connection from: " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        logger.info(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected, code:" + code);
    }

    @Override
    public void onMessage(WebSocket webSocket, String msg) {
        logger.info(webSocket.getProtocol().getProvidedProtocol());
        Global.pluginManager.triggerHook("@onWebsocketMessage");
        JsonObject messageJson = Global.jsonParser.parse(msg).getAsJsonObject();

        String sessionID = webSocket.getProtocol().getProvidedProtocol();
        Account account = Global.database.getAccountBySession(sessionID);

        if(account == null) {
            logger.info("account is null. provided: " + sessionID);
            return;
        }
        User user = new User(account, webSocket);

        switch (messageJson.get("type").getAsString().toLowerCase(Locale.ROOT)) {
            case "create":
                ROOMS.add(new Room(UUID.randomUUID().toString(), user));
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
                for (var room : ROOMS) {
                }


            default:
                logger.info("wrong type: " + messageJson.get("type").getAsString());
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
