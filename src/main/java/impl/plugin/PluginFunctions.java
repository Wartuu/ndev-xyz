package impl.plugin;

import com.sun.net.httpserver.HttpExchange;
import impl.HttpService;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class PluginFunctions {
    private PluginManager pluginManager;
    private ConfigJson config;
    public Logger logger = LoggerFactory.getLogger(PluginFunctions.class);

    public PluginFunctions(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        this.config = Utils.getConfig(Global.configName);
    }

    public void createHook(String name, Callable hook) {
        this.pluginManager.createHook(name, hook);
    }
    public void createUri(String path, Callable function) {
        Global.httpService.httpServer.createContext(path, (HttpExchange exchange) -> {
            this.pluginManager.addToEngine("exchange", exchange);

            try {
                function.call();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            this.pluginManager.removeFromEngine("exchange");
        });
    }

    public void deleteUri(String path) {
        Global.httpService.httpServer.removeContext(path);
    }

    public void stopHttpServer() {
        Global.httpService.stop();
    }

    public void restartHttpServer() {
        logger.warn("RESTARTING HTTPSERVER");

        Global.httpService.stop();
        Global.httpService = new HttpService(config);
        Global.httpService.start();
    }

    public ConfigJson getConfig() {
        return config;
    }
}
