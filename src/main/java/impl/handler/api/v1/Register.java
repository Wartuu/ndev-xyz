package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.json.account.AccountVerificationJson;
import impl.json.account.RegisterJson;
import impl.utils.AccountType;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Register implements HttpHandler {
    private Logger logger = LoggerFactory.getLogger(Register.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
            String json = Utils.getFromPost(exchange);
            logger.debug(json);
            AccountVerificationJson accessJson;
            try {
                accessJson = Global.gson.fromJson(json, AccountVerificationJson.class);
            } catch (Exception e) {
                RegisterJson registerJson = new RegisterJson();
                registerJson.setReason("wrong json");
                registerJson.setSuccess(false);
                registerJson.setAuthToken(null);
                registerJson.setSessionToken(null);

                Utils.sendOutput(exchange, Global.gson.toJson(registerJson, RegisterJson.class), false, 200);
                return;
            }

            Utils.sendOutput(exchange, Global.gson.toJson(Global.database.createNewAccount(exchange, accessJson.getUsername(), accessJson.getPassword(), AccountType.NORMAL.getAccountType())), false, 200);


        }

    }
}
