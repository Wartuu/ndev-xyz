package impl.handler.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;

import java.io.IOException;

public class Robots implements HttpHandler {
    private String robotsFile;

    public Robots(String robotsFile) {
        this.robotsFile = robotsFile;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, robotsFile, false, 200);
    }
}
