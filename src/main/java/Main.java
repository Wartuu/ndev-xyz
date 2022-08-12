import impl.HttpService;
import impl.WebsocketService;
import impl.json.Config;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    protected static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        logger.info("loading config...");
        Config config = Utils.getConfig(Global.configName);

        logger.info("starting http service at port: " + config.getHttpPort());
        Thread httpServiceThread = new Thread(()->{new HttpService(config).start();});
        httpServiceThread.setName("http-service-01");
        httpServiceThread.start();

        logger.info("starting websocket service at port: " + config.getWebsocketPort());
        Thread websocketServiceThread = new Thread(()->{new WebsocketService(config).start();});
        websocketServiceThread.setName("websocket-service-01");
        websocketServiceThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("/q") || input.equalsIgnoreCase("\\q")) {
                logger.info("Stopping http service");

                logger.info("Stopping websocket service");
                websocketServiceThread.stop();

                logger.info("Application exits...");
                System.exit(0);
            };
        }





    }
}
