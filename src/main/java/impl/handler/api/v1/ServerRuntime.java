package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.html.HtmlParser;
import impl.utils.Utils;

import java.io.IOException;

public class ServerRuntime implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, HtmlParser.parseStatic("@main.runtime"), false, 200);
    }
}
