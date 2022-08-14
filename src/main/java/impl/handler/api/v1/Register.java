package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.json.AccountAccessJson;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;

public class Register implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equalsIgnoreCase("post")) {
            AccountAccessJson accessJson = Global.gson.fromJson(Utils.getFromPost(exchange), AccountAccessJson.class);
            Utils.sendOutput(exchange, Global.gson.toJson(Global.database.createNewAccount(exchange, accessJson.getUsername(), accessJson.getPassword(), 1)), false, 200);
        }


    }
}
