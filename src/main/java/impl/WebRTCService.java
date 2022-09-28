package impl;

import impl.json.ConfigJson;
import impl.utils.irc.ChatRoom;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

public class WebRTCService extends WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);

    public boolean running = false;


    private ConfigJson config;

    public WebRTCService(ConfigJson cfg, HttpService httpService) {
        super(new InetSocketAddress(cfg.getWebRtcPort()));
        this.config = cfg;

    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket webSocket, String msg) {

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        if (webSocket == null) {webSocket.close();}
    }

    @Override
    public void onStart() {
        logger.info("WebRTC service running at 127.0.0.1:" + this.getPort() + "/");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);

        running = true;
    }
}
