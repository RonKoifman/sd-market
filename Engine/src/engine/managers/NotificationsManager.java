package engine.managers;

import dto.models.NotificationDTO;
import engine.models.notification.Notification;

import java.util.Collection;

public interface NotificationsManager {

    void addNewNotificationToUser(String username, Notification newNotification);

    Collection<NotificationDTO> getNotificationsByUsername(String username);
}
