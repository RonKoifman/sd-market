package engine.models.chat;

import dto.models.ChatEntryDTO;
import engine.interfaces.Transferable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatEntry implements Transferable<ChatEntryDTO> {
    
    private final String username;
    private final String message;
    private final String time;

    public ChatEntry(String username, String message) {
        this.username = username;
        this.message = message;
        this.time = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
    }

    @Override
    public ChatEntryDTO toDTO() {
        return new ChatEntryDTO.Builder()
                .username(username)
                .message(message)
                .time(time)
                .build();
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ChatEntry{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
