package dto.models;

public class NotificationDTO {

    private final String title;
    private final String message;
    private final String date;

    private NotificationDTO(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.date = builder.date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public static final class Builder {

        private String title;
        private String message;
        private String date;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public NotificationDTO build() {
            return new NotificationDTO(this);
        }
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
