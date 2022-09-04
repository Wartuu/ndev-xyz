package impl.plugin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.HttpService;
import impl.utils.Utils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

public class PluginFunctions {
    private PluginManager pluginManager;
    private HttpService httpService;
    private Logger logger = LoggerFactory.getLogger(PluginFunctions.class);

    public PluginFunctions(PluginManager pluginManager, HttpService server) {
        this.pluginManager = pluginManager;
        this.httpService = server;
    }

    public void createHook(String hook, Callable function) {
        pluginManager.createHook(hook, function);
    }

    public void log(String s) {
        logger.info(s);
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void error(String s) {
        logger.error(s);
    }

    public void createUri(String path, String content, boolean isLarge) {
        httpService.httpServer.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                Utils.sendOutput(exchange, content, isLarge, 200);
            }
        });
    }

}
