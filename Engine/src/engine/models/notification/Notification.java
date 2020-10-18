package engine.models.notification;

import dto.models.NotificationDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Notification {

    protected final String title;
    protected final Date date;
    protected String message;

    public Notification(String title) {
        this.title = title;
        this.date = new Date(System.currentTimeMillis());
    }

    public NotificationDTO toNotificationDTO() {
        return new NotificationDTO.Builder()
                .title(title)
                .message(message)
                .date(new SimpleDateFormat("HH:mm dd/MM/yyyy").format(date))
                .build();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
