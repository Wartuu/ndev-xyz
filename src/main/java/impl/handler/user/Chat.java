package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;

import java.io.IOException;

public class Chat implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, "", false, 200);
    }
}
