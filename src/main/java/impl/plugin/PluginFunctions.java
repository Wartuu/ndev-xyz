package impl.plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.HttpService;
import impl.WebsocketService;
import impl.database.Account;
import impl.database.Database;
import impl.json.ConfigJson;
import impl.utils.Hardware;
import impl.utils.HtmlParser;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;

public class PluginFunctions {
    private PluginManager pluginManager;
    private HttpService httpService;
    private WebsocketService websocketService;
    private ConfigJson config;
    private String rawConfig;
    private JsonObject configObject;
    public Logger logger = LoggerFactory.getLogger(PluginFunctions.class);

    public PluginFunctions(PluginManager pluginManager, HttpService server, WebsocketService websocket) {
        this.pluginManager = pluginManager;
        this.httpService = server;
        this.config = Utils.getConfig(Global.configName);
        this.rawConfig = Utils.getConfigRaw(Global.configName);
        this.configObject = new JsonParser().parse(rawConfig).getAsJsonObject();
        this.websocketService = websocket;
    }

    public void createHook(String hook, Callable function) {
        pluginManager.createHook(hook, function);
    }

    public void createUri(String path, String content, boolean isLarge, String customHook, String contentType) {
        httpService.httpServer.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                pluginManager.triggerHook(customHook);
                exchange.getResponseHeaders().set("Content-type", contentType);
                Utils.sendOutput(exchange, content, isLarge, 200);

            }
        });
    }

    public void deleteUri(String path) {
        httpService.httpServer.removeContext(path);
    }

    public String getFromConfig(String value) {
        return configObject.get(value).getAsString();
    }

    public String getConfig() {
        return rawConfig;
    }

    public String getHardwareInfo(String value) {
        return Hardware.getUsage(value);
    }

    public String parseHtml(String html) {
        return  HtmlParser.parse(html);
    }

    public void stopHttpServer() {
        httpService.stop();
    }

    public void startHttpServer() {
        httpService.httpServer.start();
    }

}
