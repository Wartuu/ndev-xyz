package impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.handler.user.Chat;
import impl.json.Config;
import impl.handler.admin.AdminConsole;
import impl.handler.user.Home;
import impl.utils.Database;
import impl.utils.Utils;
import impl.utils.executor.ExecutorRejectionHandler;
import impl.utils.executor.ExecutorThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpService {

    private static ThreadPoolExecutor threadPoolExecutor;
    private static Config cfg;
    private static boolean isRunning = false;

    public static HttpServer httpServer;
    public static Database database;

    private static final Logger logger = LoggerFactory.getLogger(HttpService.class);


    public HttpService(Config config) {
        cfg = config;
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
            logger.info("Connecting to database: " + cfg.getDatabaseUrl());
            database.connect();

            logger.info("creating context");
            httpServer = HttpServer.create(new InetSocketAddress(cfg.getHttpPort()), 0);

            // user
            httpServer.createContext("/", new Home());
            httpServer.createContext("/chat", new Chat());

            // admin
            httpServer.createContext("/console", new AdminConsole());

            //static
            ArrayList<String> staticFiles = new ArrayList<>();
            try {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("static");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                logger.info("looping trough resource:/static/ - make sure no any folder in static exists");
                String resFile;
                while((resFile = bufferedReader.readLine()) != null) {
                    logger.info(resFile);
                    staticFiles.add(resFile);
                }
                inputStream.close();
                bufferedReader.close();

            } catch (Exception e) {logger.warn(e.toString());};

            for (String file : staticFiles) {
                String ext = file.substring(file.lastIndexOf('.'));
                ext = ext.substring(1);
                String path = "/static/"+ext+"/"+file;
                logger.debug(Utils.getResource(file));
                httpServer.createContext(path, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        Utils.sendOutput(exchange, Utils.getResource("static/" + file), false, 200);
                    }
                });
                logger.info("created context for /static/" + file + " in context -> " + path);
            }

            int executorCoreThreads;
            int executorOverrunThreads;
            int executorMaxIdleTime;
            int executorMaxThreadQueue;

            if(cfg.getExecutorCoreSize() == -1)
                executorCoreThreads = Runtime.getRuntime().availableProcessors();
            else executorCoreThreads = cfg.getExecutorCoreSize();

            if (cfg.getExecutorOverrunSize() == -1)
                executorOverrunThreads = Integer.MAX_VALUE;
            else executorOverrunThreads = cfg.getExecutorOverrunSize();

            executorMaxIdleTime = cfg.getExecutorMaxIdleTime();

            if(cfg.getExecutorMaxWorkQueue() == -1)
                executorMaxThreadQueue = Integer.MAX_VALUE;
            else executorMaxThreadQueue = cfg.getExecutorMaxWorkQueue();



            threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executorCoreThreads);
            threadPoolExecutor.setThreadFactory(new ExecutorThreadFactory());
            threadPoolExecutor.setCorePoolSize(executorCoreThreads);
            threadPoolExecutor.setMaximumPoolSize(executorOverrunThreads + executorCoreThreads);
            threadPoolExecutor.setKeepAliveTime(executorMaxIdleTime, TimeUnit.SECONDS);
            threadPoolExecutor.setRejectedExecutionHandler(new ExecutorRejectionHandler());

            httpServer.setExecutor(threadPoolExecutor);


            httpServer.start();
            logger.info("Running at http://127.0.0.1:" + cfg.getHttpPort() + "/");

        }catch (IOException e) {e.printStackTrace();}

    }



}
