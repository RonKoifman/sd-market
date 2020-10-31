package dto.models;

public class ChatEntryDTO {

    private final String username;
    private final String message;
    private final String time;

    private ChatEntryDTO(Builder builder) {
        this.username = builder.username;
        this.message = builder.message;
        this.time = builder.time;
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

    public static final class Builder {

        private String username;
        private String message;
        private String time;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public ChatEntryDTO build() {
            return new ChatEntryDTO(this);
        }
    }

    @Override
    public String toString() {
        return "ChatEntryDTO{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
