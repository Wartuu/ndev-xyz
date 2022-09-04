package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.HtmlParser;
import impl.utils.Utils;

import java.io.IOException;

public class Home implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().toString().equals("/")) {
            Utils.sendOutput(exchange, HtmlParser.parse(Utils.getResource("html/home.html")), false, 200);
        } else {
            Utils.sendOutput(exchange, HtmlParser.parse(Utils.getResource("html/404.html")), false, 200);

        }

    }
}