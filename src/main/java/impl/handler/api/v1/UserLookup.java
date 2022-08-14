package impl.handler.api.v1;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class UserLookup implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(UserLookup.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String type = Utils.getUriContentByName(exchange, "type");
        Account account = null;
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        String user = Utils.getUriContentByName(exchange, "user");
        logger.info("userlookup: " + user);

        Utils.sendOutput(exchange,Global.gson.toJson(Global.database.getAccountByUsername(user)), false, 200);


    }
}
