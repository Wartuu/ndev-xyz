package impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.handler.api.v1.*;
import impl.handler.user.Chat;
import impl.json.ConfigJson;
import impl.handler.admin.AdminConsole;
import impl.handler.user.Home;
import impl.json.VersionJson;
import impl.utils.Database;
import impl.utils.Utils;
import impl.utils.executor.ExecutorRejectionHandler;
import impl.utils.executor.ExecutorThreadFactory;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpService {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static ConfigJson config;
    public static boolean isRunning = false;

    public static HttpServer httpServer;

    private static Instant httpServiceStartTime;

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

            //static
            ArrayList<String> staticFiles = new ArrayList<>();
            try {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                logger.info("looping trough resource:/static/ - make sure no any folder in static exists");
                String resFile;
                while((resFile = bufferedReader.readLine()) != null) {
                    logger.info("added static content -> " + resFile);
                    staticFiles.add(resFile);
                }
                inputStream.close();
                bufferedReader.close();

            } catch (Exception e) {logger.warn(e.toString());};

            for (String file : staticFiles) {
                String ext = file.substring(file.lastIndexOf('.'));
                ext = ext.substring(1);
                String path = "/static/"+ext+"/"+file;
                httpServer.createContext(path, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        Utils.sendOutput(exchange, Utils.getResource("static/" + file), false, 200);
                    }
                });
                logger.info("created HttpHandler for /static/" + file + " in path -> " + path);
            }

            int executorCoreThreads;
            int executorOverrunThreads;
            int executorMaxIdleTime;
            int executorMaxThreadQueue;

            if(config.getExecutorCoreSize() == -1) {
                executorCoreThreads = Runtime.getRuntime().availableProcessors();
            } else {executorCoreThreads = config.getExecutorCoreSize();}

            if (config.getExecutorOverrunSize() == -1){
                executorOverrunThreads = Integer.MAX_VALUE - executorCoreThreads;
            } else {executorOverrunThreads = config.getExecutorOverrunSize();}

            logger.info(String.valueOf( "core-executor-threads: " + executorCoreThreads));
            logger.info(String.valueOf("overrun-executor-threads: " + executorOverrunThreads));

            executorMaxIdleTime = config.getExecutorMaxIdleTime();

            if(config.getExecutorMaxWorkQueue() == -1)
                executorMaxThreadQueue = Integer.MAX_VALUE;
            else executorMaxThreadQueue = config.getExecutorMaxWorkQueue();



            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executorCoreThreads);
            threadPoolExecutor.setThreadFactory(new ExecutorThreadFactory(config));
            threadPoolExecutor.setCorePoolSize(executorCoreThreads);
            threadPoolExecutor.setMaximumPoolSize(executorOverrunThreads + executorCoreThreads);
            threadPoolExecutor.setKeepAliveTime(executorMaxIdleTime, TimeUnit.SECONDS);
            threadPoolExecutor.setRejectedExecutionHandler(new ExecutorRejectionHandler());

            httpServer.setExecutor(threadPoolExecutor);

            httpServer.start();
            logger.info("Running at http://127.0.0.1:" + config.getHttpPort() + "/");
            logger.info("StartTime set to " +  Global.startTime.toEpochMilli());

        }catch (IOException e) {e.printStackTrace();}

    }



}
