package impl.utils.irc;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    public final List<Message> messageList = new ArrayList<>();
    public final List<String> users = new ArrayList<>();
    public final String chatRoomName;

    public ChatRoom(String name) {
        this.chatRoomName = name;
    }

}
