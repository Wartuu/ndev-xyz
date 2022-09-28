package impl;

import impl.json.ConfigJson;
import impl.utils.finals.Global;
import impl.utils.irc.ChatRoom;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class WebsocketService extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    private static final List<ChatRoom> chatRooms = new ArrayList<>();
    public boolean running = false;

    private ConfigJson config;

    public WebsocketService(ConfigJson cfg) {
        super(new InetSocketAddress(cfg.getWebsocketPort()));
        this.config = cfg;

    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("new connection from: " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        logger.info(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected code:" + code);
    }

    @Override
    public void onMessage(WebSocket webSocket, String msg) {
        Global.pluginManager.triggerHook("@onWebsocketMessage");
        broadcast(msg);
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

        if (webSocket == null) {

        }
    }

    @Override
    public void onStart() {
        logger.info("websocket service running at 127.0.0.1:" + this.getPort() + "/");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

        running = true;
    }
}
