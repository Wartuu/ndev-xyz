package impl.plugin.pluginlibraries;

import com.sun.net.httpserver.HttpExchange;
import impl.database.Account;
import impl.utils.finals.Global;

public class PluginDatabase {
    public Account getAccountBySession(String session) {
        return Global.database.getAccountBySession(session);
    }

    public void updateLastIpAndDate(HttpExchange exchange, Account account) {
        Global.database.updateLastIpAndDate(exchange, account);
    }

    public String execute(String query, String outVal) {
        return Global.database.execute(query, outVal);
    }

    public void deleteSession(Account account) {
        Global.database.deleteSession(account);
    }

    public void generateSession(Account account) {
        Global.database.generateNewSession(account);
    }
}
