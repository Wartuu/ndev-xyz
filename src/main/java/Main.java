import impl.HttpService;
import impl.WebsocketService;
import impl.json.ConfigJson;
import impl.plugin.PluginManager;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static WebsocketService websocketService = null;
    public static HttpService httpService = null;

    public static void main(String[] args) throws Exception {

        logger.info("loading config...");
        ConfigJson configGson = Utils.getConfig(Global.configName);

        logger.info("starting websocket service at port: " + configGson.getWebsocketPort());
        Thread websocketServiceThread = new Thread(()->{websocketService = new WebsocketService(configGson); websocketService.start();});
        websocketServiceThread.setName("websocket-service-01");
        websocketServiceThread.start();

        logger.info("starting http service at port: " + configGson.getHttpPort());
        Thread httpServiceThread = new Thread(()->{httpService = new HttpService(configGson); httpService.start();});
        httpServiceThread.setName("http-service-01");
        httpServiceThread.start();

        logger.info("starting plugin manager");
        Global.pluginManager = new PluginManager(httpService, websocketService);
        Thread pluginManagerThread = new Thread(Global.pluginManager::hookLoop);
        pluginManagerThread.setName("plugin-manager");
        pluginManagerThread.start();


    }
}
