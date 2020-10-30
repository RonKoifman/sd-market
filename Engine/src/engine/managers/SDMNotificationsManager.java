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
            usernameToUserNotifications.put(username, new ArrayList<>());
        }

        usernameToUserNotifications.get(username).add(newNotification);
    }

    @Override
    public Collection<NotificationDTO> getNotificationsFromIndexByUsername(String username, int fromIndex) {
        return Collections.unmodifiableCollection(usernameToUserNotifications.get(username)
                .stream()
                .map(Notification::toDTO)
                .skip(fromIndex)
                .collect(Collectors.toList()));
    }

    @Override
    public int getNotificationsVersionByUsername(String username) {
        return usernameToUserNotifications.get(username).size();
    }

    @Override
    public String toString() {
        return "SDMNotificationsManager{" +
                "usernameToUserNotifications=" + usernameToUserNotifications +
                '}';
    }
}
