package impl.handler.api.v1;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;
import java.util.Objects;

public class UserLookup implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String type = Utils.getUriContentByName(exchange, "type");
        Account account = null;
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if (Objects.equals(type, "username")) {
            String username = Utils.getUriContentByName(exchange, "search");
            account = Global.database.getAccountByUsername(username);
        } else if (Objects.equals(type, "id")) {
            long id = Integer.parseInt(Utils.getUriContentByName(exchange, "search"));
            account = Global.database.getAccountById(id);
        }

        Gson gson = new Gson();
        Utils.sendOutput(exchange,gson.toJson(account), false, 200);


    }
}
