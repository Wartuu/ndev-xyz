package impl.utils.irc;

import impl.database.Account;
import org.java_websocket.WebSocket;

public class User {
    public final Account account;
    public WebSocket webSocket;

    public User(Account account, WebSocket webSocket) {
        this.account = account;
        this.webSocket = webSocket;
    }
}
