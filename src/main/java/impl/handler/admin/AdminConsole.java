package impl.handler.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.HttpService;
import impl.utils.HtmlParser;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AdminConsole implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(AdminConsole.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.sendOutput(exchange, HtmlParser.parse(Utils.getResource("html/console.html")), false, 200);
    }
}
