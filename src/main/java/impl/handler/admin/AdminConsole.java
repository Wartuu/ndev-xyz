package impl.handler.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AdminConsole implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(AdminConsole.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }
}
