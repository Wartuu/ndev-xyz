package impl.handler.vue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;

public class UserRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Account account = Global.database.getAccountBySession(Utils.getCurrentSession(exchange));


        if(account != null) {
            Global.database.updateLastIpAndDate(exchange, account);
        }

        Utils.sendOutput(exchange, Utils.getResource("html/vue-user.html"), false, 200);
    }
}
