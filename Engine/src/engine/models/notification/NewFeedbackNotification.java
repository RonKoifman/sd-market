package engine.models.notification;

import engine.enums.Rating;

public class NewFeedbackNotification extends Notification {

    private final String username;
    private final String storeName;
    private final Rating rating;

    public NewFeedbackNotification(String username, String storeName, Rating rating) {
        super("New Feedback Received");
        this.username = username;
        this.rating = rating;
        this.storeName = storeName;
        this.message = buildMessage();
    }

    public String getUsername() {
        return username;
    }

    private String buildMessage() {
       return String.format("You've just received a new feedback from the user '%s'!" + System.lineSeparator() +
               "Your store '%s' got a rating of %d stars.",
               username, storeName, rating.getOrdinal());
    }

    @Override
    public String toString() {
        return "NewFeedbackNotification{" +
                "username='" + username + '\'' +
                ", storeName='" + storeName + '\'' +
                ", rating=" + rating +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
