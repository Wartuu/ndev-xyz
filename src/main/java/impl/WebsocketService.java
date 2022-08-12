package impl;

import impl.json.Config;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class WebsocketService extends WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    private Config cfg;

    public WebsocketService(Config config) {
        super(new InetSocketAddress(config.getWebsocketPort()));
        cfg = config;

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
        broadcast(msg);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer msg) {
        broadcast(msg.array());
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error("error has occurred: " + e.toString());

        if (webSocket == null)
            webSocket.close();
    }

    @Override
    public void onStart() {
        logger.info("websocket service running at 127.0.0.1:" + this.getPort() + "/");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}
