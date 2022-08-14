package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.HtmlParser;
import impl.utils.Utils;

import java.io.IOException;

public class Home implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String postBody = Utils.getFromPost(exchange);
        Utils.sendOutput(exchange, HtmlParser.parse(postBody), false, 200);
    }
}
