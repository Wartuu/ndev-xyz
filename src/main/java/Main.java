import impl.HttpService;
import impl.WebsocketService;
import impl.json.ConfigJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("loading config...");
        ConfigJson configGson = Utils.getConfig(Global.configName);


        logger.info("starting http service at port: " + configGson.getHttpPort());
        Thread httpServiceThread = new Thread(()->{new HttpService(configGson).start();});
        httpServiceThread.setName("http-service-01");
        httpServiceThread.start();

        logger.info("starting websocket service at port: " + configGson.getWebsocketPort());
        Thread websocketServiceThread = new Thread(()->{new WebsocketService(configGson).start();});
        websocketServiceThread.setName("websocket-service-01");
        websocketServiceThread.start();

    }
}
