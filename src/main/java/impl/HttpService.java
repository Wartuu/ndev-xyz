package impl;

import com.sun.net.httpserver.HttpServer;
import impl.handler.api.v1.*;
import impl.handler.user.*;
import impl.json.ConfigJson;
import impl.handler.admin.AdminConsole;
import impl.json.VersionJson;
import impl.database.Database;
import impl.utils.Utils;
import impl.utils.finals.Global;
import impl.plugin.PluginManager;
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


    public HttpService(ConfigJson cfg) {
        config = cfg;
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

            // user
            httpServer.createContext("/", new Home());
            httpServer.createContext("/chat", new Chat());
            httpServer.createContext("/tos", new Tos());
            httpServer.createContext("/login", new LoginPage());
            httpServer.createContext("/register", new RegisterPage());

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


            // admin
            httpServer.createContext("/admin/console", new AdminConsole());

            Utils.loadStaticHandlers(httpServer);

            threadPoolExecutor = Utils.getThreadpoolExecutor();
            httpServer.setExecutor(threadPoolExecutor);

            PluginManager pluginManager = new PluginManager(this);
            Thread pluginManagerThread = new Thread(pluginManager::hookLoop);
            pluginManagerThread.setPriority(Thread.MIN_PRIORITY);
            pluginManagerThread.setName("Plugin-manager");
            pluginManagerThread.start();

            httpServer.start();
            logger.info("Running at http://127.0.0.1:" + config.getHttpPort() + "/");
            logger.info("StartTime set to " +  Global.startTime.toString());

        }catch (IOException e) {e.printStackTrace();}

    }



}
