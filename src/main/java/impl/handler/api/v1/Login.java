package impl.handler.api.v1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.json.AccountAccessJson;
import impl.json.LoginJson;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Login implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equalsIgnoreCase("post")) {

            AccountAccessJson accessJson = Global.gson.fromJson(Utils.getFromPost(exchange), AccountAccessJson.class);
            LoginJson outputJson = new LoginJson();



            if(Global.database.containsIllegalCharacter(accessJson.getUsername())) {
                logger.info("illegal character or user does not exist");
                outputJson.setReason("input contains illegal character");
                outputJson.setSuccess(false);
                Utils.sendOutput(exchange, Global.gson.toJson(outputJson), false, 200);
                return;
            }

            Account userAccount = Global.database.getAccountByUsername(accessJson.getUsername());

            String salt = userAccount.getPassword().split(":")[0];
            String hash = userAccount.getPassword().split(":")[1];

            String inputHash = Utils.sha256(accessJson.getPassword(), Utils.hexToByte(salt));

            if(hash.equals(inputHash)) {
                logger.info(userAccount.getUsername() + " logged at ip: " + exchange.getRemoteAddress().getAddress().getHostAddress());
                Global.database.update("update account " +
                                                "set last_login = now() " +
                                                "where id=" + userAccount.getId() );
                Global.database.update("update account " +
                        "set last_ip = '" + exchange.getRemoteAddress().getAddress().getHostAddress() + "' " +
                        "where id=" + userAccount.getId() );


                if(userAccount.getSession() == null) {
                    userAccount = Global.database.generateNewSession(userAccount);
                }

                outputJson.setSession(userAccount.getSession());
                outputJson.setSuccess(true);
                Utils.sendOutput(exchange, Global.gson.toJson(outputJson), false, 200);
            } else {

                outputJson.setReason("wrong password/username or input contains illegal character");
                outputJson.setSuccess(false);
                Utils.sendOutput(exchange, Global.gson.toJson(outputJson), false, 200);
            }

        }
    }
}
