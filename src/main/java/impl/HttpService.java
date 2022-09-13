package impl;

import com.sun.net.httpserver.HttpServer;
import impl.handler.api.v1.*;
import impl.handler.support.Robots;
import impl.handler.react.AdminRouter;
import impl.handler.react.UserRouter;
import impl.json.ConfigJson;
import impl.json.VersionJson;
import impl.database.Database;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpService {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConfigJson config;
    public static boolean isRunning = false;

    public HttpServer httpServer;

    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);
    private static String userRouterPage;
    private static String adminRouterPage;


    public HttpService(ConfigJson cfg) {
        config = cfg;
        userRouterPage = Utils.getResource("html/react-user.html");
        adminRouterPage = Utils.getResource("html/react-admin.html");
    }

    public void stop() {
        if(isRunning && httpServer != null) {
            httpServer.stop(0);
            httpServer = null;
            logger.info("http service stopped");
        }
    }

    public void start() {
        try{
            Global.database = new Database(config);
            Global.database.connect();

            logger.info("creating context");
            httpServer = HttpServer.create(new InetSocketAddress(config.getHttpPort()), 0);

            //api

            VersionJson versionJson = new VersionJson();
            versionJson.setFrontendVersion(config.getFrontendVersion());
            versionJson.setBackendVersion(config.getBackendVersion());
            versionJson.setMainVersion(config.getMainVersion());

            httpServer.createContext("/api/v1/version", new Version(versionJson));
            httpServer.createContext("/api/v1/runtime", new ServerRuntime());
            httpServer.createContext("/api/v1/lookup", new UserLookup());
            httpServer.createContext("/api/v1/register", new Register());
            httpServer.createContext("/api/v1/login", new Login());
            httpServer.createContext("/api/v1/logout", new Logout());

            // vue.js
            httpServer.createContext("/", new UserRouter(userRouterPage));
            httpServer.createContext("/admin", new AdminRouter(userRouterPage, adminRouterPage));
            httpServer.createContext("/robots.txt", new Robots());

            Utils.loadStaticHandlers(httpServer);

            threadPoolExecutor = Utils.getThreadpoolExecutor();
            httpServer.setExecutor(threadPoolExecutor);

            httpServer.start();
            logger.info("Running at http://127.0.0.1:" + config.getHttpPort() + "/");
            logger.info("StartTime set to " +  Global.startTime.toString());

        } catch (IOException e) {e.printStackTrace();}
    }



}
