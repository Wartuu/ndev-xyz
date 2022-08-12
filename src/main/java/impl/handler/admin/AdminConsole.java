package impl.handler.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Database;
import impl.utils.Utils;

import java.io.IOException;

public class AdminConsole implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, "OK", true, 200);
    }
}
