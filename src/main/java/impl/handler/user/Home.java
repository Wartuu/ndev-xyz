package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;

import java.io.IOException;

public class Home implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, Utils.getFromPost(exchange).toString(), false, 200);
    }
}
