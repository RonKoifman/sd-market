package engine.managers;

import dto.models.NotificationDTO;
import engine.models.notification.Notification;

import java.util.*;
import java.util.stream.Collectors;

public class SDMNotificationsManager implements NotificationsManager {

    private static SDMNotificationsManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final Map<String, List<Notification>> usernameToUserNotifications = new HashMap<>();

    private SDMNotificationsManager() {
    }

    public static NotificationsManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMNotificationsManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void addNewNotificationToUser(String username, Notification newNotification) {
        if (!usernameToUserNotifications.containsKey(username)) {
            usernameToUserNotifications.put(username, new LinkedList<>());
        }

        usernameToUserNotifications.get(username).add(newNotification);
    }

    @Override
    public Collection<NotificationDTO> getNotificationsByUsername(String username) {
        return Collections.unmodifiableCollection(usernameToUserNotifications.get(username)
                .stream()
                .map(Notification::toNotificationDTO)
                .collect(Collectors.toList()));
    }
}
