package impl.plugin.pluginlibraries;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.plugin.PluginManager;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class PluginHTTP {

    private final PluginManager pluginManager;
    public PluginHTTP(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void sendOutput(HttpExchange exchange, String output, boolean isLarge, int responseCode) {
        Utils.sendOutput(exchange, output, isLarge, responseCode);
    }

    public String getSession(HttpExchange exchange) {
        return Utils.getCurrentSession(exchange);
    }

    public void createContext(String uri, Callable handler) {
        Global.httpService.httpServer.createContext(uri, new HttpHandler() {
            private final Logger logger = LoggerFactory.getLogger("plugin-" + uri);
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    pluginManager.addToEngine("exchange", exchange);
                    handler.call();
                    pluginManager.removeFromEngine("exchange");
                } catch (Exception e) {logger.error(e.getMessage());}
            }
        });
    }
}
