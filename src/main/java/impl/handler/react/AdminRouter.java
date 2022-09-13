package impl.handler.react;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import impl.database.Account;
import impl.utils.AccountType;
import impl.utils.HtmlParser;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AdminRouter implements HttpHandler {

    private Logger logger = LoggerFactory.getLogger(AdminRouter.class);
    private String userPage;
    private String adminPage;

    public AdminRouter(String userPage, String adminPage) {
        this.userPage = HtmlParser.parse(userPage);
        this.adminPage = HtmlParser.parse(adminPage);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Account account = Global.database.getAccountBySession(Utils.getCurrentSession(exchange));

        if(account == null) {
            Utils.sendOutput(exchange, userPage, false, 200);
            return;
        } else {
            Global.database.updateLastIpAndDate(exchange, account);
        }

        if(account.getAccountType() >= AccountType.ADMIN.getAccountType()) {
            Utils.sendOutput(exchange, Utils.getResource(adminPage), false, 200);
        } else {
            Utils.sendOutput(exchange, Utils.getResource(userPage), false, 200);
        }
    }
}
