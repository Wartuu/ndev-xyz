package impl.utils.irc;

import com.sun.net.httpserver.HttpExchange;
import impl.database.Account;
import impl.utils.Utils;
import impl.utils.finals.Global;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final Logger logger = LoggerFactory.getLogger(Room.class);
    public final String ID;

    public final List<User> users = new ArrayList<>();

    public boolean isPrivate;


    public Room(String ID, User owner, boolean isPublic) {
        this.ID = ID;
        this.users.add(owner);

        logger.info("owner: " + owner.account.getUsername());

        Context context = Global.templateUtils.getDefaultContext();
        context.setVariable("roomId", ID);

        String page = Global.templateUtils.engine.process("chat-room", context);





        Global.httpService.httpServer.createContext("/chat/" + ID, (HttpExchange exchange) -> {
            Account visitor = Global.database.getAccountBySession(Utils.getCurrentSession(exchange));


            if(visitor != null) {
                logger.info("user connecting: " + visitor.getUsername());
                for (var user : users) {
                    if(user.account.getAuthToken().equals(visitor.getAuthToken())) {
                        Context contextDynamic = new Context();
                        contextDynamic.setVariable("username", Global.database.getAccountBySession(Utils.getCurrentSession(exchange)).getUsername());
                        Utils.sendOutput(exchange, Global.templateUtils.engine.process("chat-room", contextDynamic), false, 200);
                    }
                }
            }
        });
    }

    public Room(String ID, boolean isPublic) {
        this.ID = ID;
        Context contextStatic = Global.templateUtils.getDefaultContext();
        contextStatic.setVariable("roomId", ID);

        String page = Global.templateUtils.engine.process("chat-room", contextStatic);

        Global.httpService.httpServer.createContext("/chat/" + ID, (HttpExchange exchange) -> {
            Account visitor = Global.database.getAccountBySession(Utils.getCurrentSession(exchange));

            if(visitor != null) {
                Context contextDynamic = new Context();
                contextDynamic.setVariable("username", Global.database.getAccountBySession(Utils.getCurrentSession(exchange)).getUsername());
                Utils.sendOutput(exchange, Global.templateUtils.engine.process("chat-room", contextDynamic), false, 200);
            }
        });
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
                if(user.webSocket.isOpen()) {
                    user.webSocket.send(msg);
                } else {
                    users.remove(user);
                }

            } else {
                users.remove(user);
            }
        }
    }


    public void prepareDelete() {
        broadcast("server is closing...");
        Global.httpService.httpServer.removeContext("/chat/" + ID);

        users.clear();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean containsWebsocket(WebSocket webSocket) {
        for (var user : users) {
            if(user.webSocket.equals(webSocket)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAccount(Account account) {
        for (var user : users) {
            if(user.account.equals(account)) {
                return true;
            }
        }
        return false;
    }



}
