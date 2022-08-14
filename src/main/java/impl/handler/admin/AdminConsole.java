package impl.handler.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.utils.Database;
import impl.utils.HtmlParser;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.rmi.CORBA.Util;
import java.io.IOException;

public class AdminConsole implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(AdminConsole.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }
}
