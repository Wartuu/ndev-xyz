package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;

import java.io.IOException;

public class Tos implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, Utils.getResource("html/tos.html"), false, 200);

    }
}
