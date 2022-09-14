package impl.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.json.ConfigJson;
import impl.utils.executor.ExecutorRejectionHandler;
import impl.utils.executor.ExecutorThreadFactory;
import impl.utils.finals.Global;
import impl.utils.gzip.Gzip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String getFromPost(HttpExchange exchange)
    {
        String out;
        String in = "";
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody());
        try {
            while (inputStreamReader.ready()) {
                in = in + (char)inputStreamReader.read();
            }
        } catch (Exception exception) {
            logger.warn(exception.toString());
        }
        return in;
    }

    public static boolean sendOutput(HttpExchange exchange, String output, boolean byteByByte ,int rCode) {
        logger.debug("Sending output with " + output.getBytes().length + "b, byteByByte: " + byteByByte + ", uri: " + exchange.getRequestURI());
        try {
            if(byteByByte) {
                exchange.sendResponseHeaders(rCode, output.length());
                OutputStream outputStream = exchange.getResponseBody();

                byte[] outputByteArray = output.getBytes();
                for (int i = 0; i < outputByteArray.length; i++) {
                    outputStream.write(outputByteArray[i]);
                }
                outputStream.close();
                return true;
            }
            else {
                exchange.sendResponseHeaders(rCode, output.length());
                OutputStream outputStream = exchange.getResponseBody();

                outputStream.write(output.getBytes());
                outputStream.close();
                return true;
            }

        } catch (Exception exception) {logger.warn(exception.toString());}
        return false;
    }

    public static boolean sendBytesOutput(HttpExchange exchange, byte[] content, int rCode) {
        try {
            logger.debug("Sending output with " + content.length + "b, uri: " + exchange.getRequestURI());
            OutputStream outputStream = exchange.getResponseBody();
            exchange.sendResponseHeaders(rCode, content.length);
            outputStream.write(content);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {logger.error(e.getMessage());}
        return false;
    }
    public static boolean gzipOutput(HttpExchange exchange, String content, int rCode) {

        exchange.getResponseHeaders().set("Content-Encoding:", "gzip");
        OutputStream outputStream = exchange.getResponseBody();
        logger.info("+---| sending gzip, size before compression: " + content.getBytes().length + "b");
        byte[] gzip = Gzip.createGzip(content);

        try {
            exchange.sendResponseHeaders(rCode, gzip.length);
            outputStream.write(gzip);
            outputStream.close();
            return true;
        } catch (Exception e) {logger.error(e.getMessage());}
        return false;
    }


    public static String getResource(String file) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fileStream = classLoader.getResourceAsStream(file);
        InputStreamReader fileReader = new InputStreamReader(fileStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);

        StringBuilder rawFile = new StringBuilder();

        try {
            for(String line; (line = reader.readLine()) != null;) {
                rawFile.append(line);
            }
        } catch (Exception e) {logger.info(e.getMessage());}


        return rawFile.toString();
    }

    public static byte[] getResourceAsBytes(String file) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fileStream = classLoader.getResourceAsStream(file);

        try {
            assert fileStream != null;
            return fileStream.readAllBytes();
        } catch (Exception e) {logger.error(e.getMessage());}
        return null;
    }


    public static String getFile(String path) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            for (String line : lines) {
                builder.append(line);
            }

            return builder.toString();

        } catch (Exception e) {logger.error(e.getMessage());}



        return builder.toString();
    }

    public static ConfigJson getConfig(String configName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream configFileStream = classLoader.getResourceAsStream(configName);
            InputStreamReader configFileSR = new InputStreamReader(configFileStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(configFileSR);

            StringBuilder rawConfig = new StringBuilder();

            for(String line; (line = reader.readLine()) != null;) {
                rawConfig.append(line);
            }

            Gson gson = new Gson();
            ConfigJson configGson = gson.fromJson(String.valueOf(rawConfig), ConfigJson.class);

            configFileSR.close();
            configFileStream.close();
            reader.close();

            return configGson;

        } catch (IOException exception) {
            logger.error("no config file or contains error " + exception.toString());
        }

        return null;
    }

    public static String getConfigRaw(String configName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream configFileStream = classLoader.getResourceAsStream(configName);
            InputStreamReader configFileSR = new InputStreamReader(configFileStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(configFileSR);

            StringBuilder rawConfig = new StringBuilder();

            for(String line; (line = reader.readLine()) != null;) {
                rawConfig.append(line);
            }

            configFileSR.close();
            configFileStream.close();
            reader.close();

            return rawConfig.toString();

        } catch (IOException exception) {
            logger.error("no config file or contains error " + exception.toString());
        }

        return null;
    }

    public static String getQuery(HttpExchange exchange, String UriName) {
        String[] query = exchange.getRequestURI().getQuery().split("&");
        List<String> queryfull = new ArrayList<String>(query.length*2);
        for (int i = 0; i < query.length;i++) {
            Collections.addAll(queryfull, query[i].split("="));
        }
        for (int i = 0; i < queryfull.size();i++) {
            if(i % 2 == 0)
                if(queryfull.get(i).equalsIgnoreCase(UriName))
                    return queryfull.get(i+1);

        }
        return "NULL";
    }

    public static String getFromJson(String json, String searchedVar) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        return jsonObject.get(searchedVar).getAsString();
    }
    public static void createResourceHandler(HttpServer server, String path, boolean isLarge) {
        server.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                Utils.sendOutput(exchange, Utils.getResource(path), isLarge, 200);
            }
        });
    }

    public static String sha512(String content, byte[] salt) {
        String out = null;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);

            final byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

            out = byteToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        return out;
    }

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static String byteToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2*bytes.length);

        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
    public static byte[] hexToByte(String str) {
        byte[] val = new byte[str.length() / 2];

        for(int i = 0; i < val.length; i++) {
            int index = i * 2;
            int convert = Integer.parseInt(str.substring(index, index + 2), 16);
            val[i] = (byte) convert;
        }
        return val;
    }

    public static String getCurrentSession(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        String session = null;

        if(cookies == null) {return null;}

        for (String cookie : cookies) {
            if(cookie.contains("session-token")) {
                session = cookie.split("=")[1];
            }
        }

        return session;
    }


    public static ThreadPoolExecutor getThreadpoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor;
        ConfigJson config = getConfig(Global.configName);

        int executorCoreThreads;
        int executorOverrunThreads;
        int executorMaxIdleTime;

        if(config.getExecutorCoreSize() == -1) {
            executorCoreThreads = Runtime.getRuntime().availableProcessors();
        } else {executorCoreThreads = config.getExecutorCoreSize();}

        if (config.getExecutorOverrunSize() == -1){
            executorOverrunThreads = Integer.MAX_VALUE - executorCoreThreads;
        } else {executorOverrunThreads = config.getExecutorOverrunSize();}

        logger.info("core-executor-threads: " + executorCoreThreads);
        logger.info("overrun-executor-threads: " + executorOverrunThreads);
        executorMaxIdleTime = config.getExecutorMaxIdleTime();



        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executorCoreThreads);
        threadPoolExecutor.setThreadFactory(new ExecutorThreadFactory(config));
        threadPoolExecutor.setCorePoolSize(executorCoreThreads);
        threadPoolExecutor.setMaximumPoolSize(executorOverrunThreads + executorCoreThreads);
        threadPoolExecutor.setKeepAliveTime(executorMaxIdleTime, TimeUnit.SECONDS);
        threadPoolExecutor.setRejectedExecutionHandler(new ExecutorRejectionHandler());

        return threadPoolExecutor;
    }

    public static ArrayList<String> getStaticFiles() {
        ArrayList<String> staticFiles = new ArrayList<>();
        File directory = new File("static");
        File[] plugins = directory.listFiles();
        try {
            for (int i = 0; i<plugins.length;i++) {
                if (plugins[i].isFile()) {
                    staticFiles.add(plugins[i].getName());
                }
            }
        } catch (Exception e) {logger.warn(e.toString()); return null;};

        return staticFiles;
    }

    public static ArrayList<String> getPlugins() {
        ArrayList<String> staticFiles = new ArrayList<>();
        File directory = new File("plugins");
        File[] plugins = directory.listFiles();
        try {
            for (int i = 0; i<plugins.length;i++) {
                if (plugins[i].isFile()) {
                    staticFiles.add(plugins[i].getName());
                }
            }
        } catch (Exception e) {logger.warn(e.toString()); return null;};

        return staticFiles;
    }

    public static boolean doesSupportGzip(HttpExchange exchange) {
        List<String> encodings = exchange.getRequestHeaders().get("Accept-Encoding");

        if(encodings == null) {return false;}

        for (String encode : encodings) {
            if(encode.toLowerCase(Locale.ROOT).contains("gzip")) {
                return true;
            }
        }
        return false;
    }


    public static void loadStaticHandlers(HttpServer httpServer) {

        for (String file : Utils.getStaticFiles()) {
            String ext = file.substring(file.lastIndexOf('.'));
            ext = ext.substring(1);
            String path = "/static/" +ext+"/"+file;
            String content = Utils.getFile("static/" + file);
            byte[] compressed = Gzip.createGzip(Utils.getFile("static/" + file));
            String mimeType = getMimeMap().get(ext);

            logger.info(file + ": uncompressed: " + content.getBytes(StandardCharsets.UTF_8).length + ", compressed: " + compressed.length);

            if(content.getBytes().length > compressed.length) {
                logger.info("using compression for: " + file);
                httpServer.createContext(path, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {


                        if(doesSupportGzip(exchange)) {
                            exchange.getResponseHeaders().set("Content-Encoding", "gzip");
                            exchange.getResponseHeaders().set("Content-Type", mimeType);
                            exchange.getResponseHeaders().set("Transfer-Encoding", "gzip");
                            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                            Utils.sendBytesOutput(exchange, compressed, 200);

                        } else {
                            exchange.getResponseHeaders().set("Content-Type", mimeType);
                            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                            Utils.sendOutput(exchange, content, false, 200);
                        }





                    }
                });
            } else {
                logger.info("using default for: " + file);
                httpServer.createContext(path, new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        exchange.getResponseHeaders().add("Content-Type", mimeType);
                        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                        Utils.sendOutput(exchange, content, false, 200);
                    }
                });
            }
        }
    }

    public static HashMap<String,String> getMimeMap() {
        HashMap<String, String> mimes = new HashMap<>();

        mimes.put("js", "application/javascript");
        mimes.put("css", "text/css");

        return mimes;
    }

}
