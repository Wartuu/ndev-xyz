package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.json.account.LogoutJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Logout implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(Logout.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LogoutJson logoutJson = new LogoutJson();

        String session = Utils.getCurrentSession(exchange);
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if(session == null) {
            logoutJson.setReason("no existing session-token");
            logoutJson.setSuccess(false);
            Utils.sendOutput(exchange, Global.gson.toJson(logoutJson), false, 200);
            return;
        }

        Account account = Global.database.getAccountBySession(session);

        if(account == null) {
            logoutJson.setReason("valid session-token");
            logoutJson.setSuccess(false);
            Utils.sendOutput(exchange, Global.gson.toJson(logoutJson), false, 200);
            return;
        }

        account = Global.database.deleteSession(account);

        logger.info("6");
        if(account.getSession() == null) {
            logoutJson.setSuccess(true);
        }
        Utils.sendOutput(exchange, Global.gson.toJson(logoutJson), false, 200);
        return;
    }
}
