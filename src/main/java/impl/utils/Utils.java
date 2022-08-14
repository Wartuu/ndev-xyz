package impl.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import impl.json.ConfigJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

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

    public static boolean sendOutput(HttpExchange exchange, String output, boolean isLarge ,int rCode) {
        logger.debug("Sending output with " + output.getBytes().length + " bytes! " + "isLarge: " + isLarge);
        try {
            if(isLarge) {
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


    public static String getResource(String file) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(file);

        if (inputStream == null) {return "";}
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        StringBuilder output = new StringBuilder();

        try {
            for(String line; (line = reader.readLine()) != null;) {
                output.append(line);
            }
            return output.toString();
        } catch (IOException e) {
            logger.warn(e.toString());
        }

        return output.toString();
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

    public static String getUriContentByName(HttpExchange exchange, String UriName) {
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

    public static String sha256(String content) {
        String out = null;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2*hash.length);

            for(int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            out = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        return out;
    }


}
