package impl.handler.react;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.utils.HtmlParser;
import impl.utils.Utils;
import impl.utils.finals.Global;

import java.io.IOException;

public class UserRouter implements HttpHandler {
    private String userPage;

    public UserRouter(String userPage) {
        this.userPage = userPage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Account account = Global.database.getAccountBySession(Utils.getCurrentSession(exchange));


        if(account != null) {
            Global.database.updateLastIpAndDate(exchange, account);
        }

        Utils.sendOutput(exchange, HtmlParser.parse(userPage), false, 200);
    }
}
