import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Slf4jReporter;
import impl.HttpService;
import impl.WebsocketService;import impl.json.ConfigJson;
import impl.plugin.PluginManager;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Main {
    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {


        if(System.getProperty("user.name").equalsIgnoreCase("admin") || System.getProperty("user.name").equalsIgnoreCase("root")) {
            for (int i = 10; i > 0; i--) {
                logger.warn("YOU ARE TRYING TO RUN NEKODEV AS ROOT/ADMIN PLEASE WAIT " + i + " SECONDS");
                Thread.sleep(1000);
            }
        }

        long startTime = System.nanoTime();
        logger.info("loading config...");
        ConfigJson configGson = Utils.getConfig(Global.configName);

        logger.info("starting http service at port: " + configGson.getHttpPort());
        Thread httpServiceThread = new Thread(()->{Global.httpService = new HttpService(configGson); Global.httpService.start();});
        httpServiceThread.setName("http-service-01");
        httpServiceThread.setPriority(Thread.MAX_PRIORITY);
        httpServiceThread.start();

        logger.info("starting websocket service at port: " + configGson.getWebsocketPort());
        Thread websocketServiceThread = new Thread(()->{Global.websocketService = new WebsocketService(configGson); Global.websocketService.start();});
        websocketServiceThread.setName("websocket-service-01");
        websocketServiceThread.setPriority(Thread.MAX_PRIORITY);
        websocketServiceThread.start();

        while (true) {
            try {
                if(Global.httpService.running && Global.websocketService.running) {
                    logger.info("starting request per second counter");

                    Slf4jReporter consoleReporter = Slf4jReporter.forRegistry(Global.metricRegistry)
                            .convertRatesTo(TimeUnit.MINUTES)
                            .convertDurationsTo(TimeUnit.SECONDS)
                            .build();
                    consoleReporter.start(configGson.getRequestMeterDelay(), TimeUnit.SECONDS);
                    break;
                }

                Thread.sleep(1);
            } catch (Exception ignored){}

        }

        logger.info("starting plugin manager");
        Global.pluginManager = new PluginManager();
        Thread pluginManagerThread = new Thread(Global.pluginManager::hookLoop);
        pluginManagerThread.setName("plugin-manager");
        pluginManagerThread.start();

        while (true) {
            try {
                if(Global.httpService.running && Global.websocketService.running && Global.pluginManager.running) {
                    break;
                }
                Thread.sleep(1);
            } catch (Exception ignored){}

        }

        long finishTime = System.nanoTime() - startTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        logger.info("started in " + simpleDateFormat.format(new Date(finishTime/1000000)));
    }
}
