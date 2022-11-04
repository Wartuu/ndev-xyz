package impl.handler.user;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;
import java.util.ArrayList;

public class Home implements HttpHandler {

    private final String content;
    public Home(String template) {
        this.content = Global.templateUtils.processDefault(template);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, content, false, 200);
    }
}
