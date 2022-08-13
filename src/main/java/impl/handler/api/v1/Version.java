package impl.handler.api.v1;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.json.VersionJson;
import impl.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Version implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(Version.class);

    private static String version;

    public Version(VersionJson versionGson) {
        Gson gson = new Gson();
        version = gson.toJson(versionGson);


    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        Utils.sendOutput(exchange, version, false, 200);
    }
}
