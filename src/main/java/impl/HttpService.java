package impl;

import com.sun.net.httpserver.HttpServer;
import impl.handler.api.v1.*;
import impl.handler.support.Favicon;
import impl.handler.support.Robots;
import impl.handler.user.Home;
import impl.handler.user.NoteSuccess;
import impl.json.ConfigJson;
import impl.json.VersionJson;
import impl.database.Database;
import impl.utils.notesbin.NotesBin;
import impl.utils.Utils;
import impl.utils.finals.Global;
import impl.utils.template.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Thymeleaf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpService {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConfigJson config;
    public boolean running = false;

    public HttpServer httpServer;

    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);
    private static byte[] faviconContent;
    private static String robotsContent;


    public HttpService(ConfigJson cfg) {
        config = cfg;
        faviconContent = Utils.getResourceAsBytes("favicon.ico");
        robotsContent = Utils.getResource("robots.txt");
    }

    public void stop() {
        if(running && httpServer != null) {
            httpServer.stop(0);
            httpServer = null;
            logger.info("http service stopped");
        }
    }

    public void start() {
        try{

            logger.info("using template thymeleaf version: " + Thymeleaf.getVersion());
            logger.info("thymeleaf stable: " + Thymeleaf.isVersionStableRelease());
            Global.templateUtils = new TemplateUtils(config);


            Global.database = new Database(config);
            Global.database.connect();

            Global.mailService = new MailClient(config);
            Global.mailService.connect();

            if (config.isNotesBinSupport()) {
                Global.notesBin = new NotesBin(config.getNotesBinUrl(), config.getNotesBinId(), config.getNotesBinAuth());
            }




            logger.info("creating context");
            httpServer = HttpServer.create(new InetSocketAddress(config.getHttpPort()), 0);
            httpServer.setExecutor(Utils.getThreadpoolExecutor());

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
            httpServer.createContext("/api/v1/create-note", new CreateNote());


            // user uri
            httpServer.createContext("/", new Home("home"));
            httpServer.createContext("/note-success", new NoteSuccess("note-success", config));


            // support
            httpServer.createContext("/robots.txt", new Robots(robotsContent));
            httpServer.createContext("/favicon.ico", new Favicon(faviconContent));


            logger.info("loading static uri...");
            Utils.loadStaticHandlers(httpServer);

            httpServer.start();
            logger.info("Running at http://127.0.0.1:" + config.getHttpPort() + "/");
            logger.info("StartTime set to " +  Global.startTime.toString());

            running = true;

        } catch (IOException e) {e.printStackTrace();}
    }



}
