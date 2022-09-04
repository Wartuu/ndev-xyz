package impl.plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.HttpService;
import impl.WebsocketService;
import impl.database.Account;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import jdk.nashorn.internal.runtime.ScriptFunction;
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

    public void createUri(String path, String content, boolean isLarge) {
        httpService.httpServer.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
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

    public String getAccountByName(String username) {
        return Global.gson.toJson(Global.database.getAccountByUsername(username), Account.class);
    }

    public String getAccountBySession(String session) {
        return Global.gson.toJson(Global.database.getAccountByUsername(session), Account.class);
    }

    public String getAccountByAuthToken(String token) {
        return Global.gson.toJson(Global.database.getAccountByAuthToken(token), Account.class);
    }

    public String getAccountById(long id) {
        return Global.gson.toJson(Global.database.getAccountById(id), Account.class);
    }

    public byte[] generateSalt() {
        return Utils.generateSalt();
    }
    public String hashWithSalt(String hashContent, byte[] salt) {
        return Utils.sha256(hashContent, salt);
    }

}
