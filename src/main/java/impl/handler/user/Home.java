package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.HtmlParser;
import impl.utils.Utils;

import java.io.IOException;

public class Home implements HttpHandler {

    private final String content;
    public Home(String path) {
        this.content = HtmlParser.parse(Utils.getFile(path));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, content, false, 200);
    }
}
