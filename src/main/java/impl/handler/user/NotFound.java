package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.html.HtmlParser;
import impl.utils.Utils;

import java.io.IOException;

public class NotFound implements HttpHandler {
    private final String content;

    public NotFound(String htmlFile) {
        this.content = htmlFile;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, HtmlParser.parse(Utils.getFile("html/404.html")), false, 200);
    }
}
