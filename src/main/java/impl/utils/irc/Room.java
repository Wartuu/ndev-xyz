package impl.utils.irc;

import impl.database.Account;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Room {
    public final String ID;

    public final List<User> users = new ArrayList<>();

    public Room(String ID, User owner) {
        this.ID = ID;
        this.users.add(owner);
    }

    public Room(String ID) {
        this.ID = ID;
    }

    public User getOwner() {
        return users.get(0);
    }

    public void updateUserWebsocket(Account account, WebSocket webSocket) {
        for (var user : users) {
            if(user.account.equals(account)) {
                user.webSocket = webSocket;
            }
        }
    }

    public void broadcast(String msg) {
        for(var user : users) {
            if(user.webSocket != null) {
                user.webSocket.send(msg);
            }
        }
    }


    public void prepareDelete() {
        broadcast("server is closing...");
    }

    public void addUser(User user) {
        users.add(user);
    }



}
