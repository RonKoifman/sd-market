package engine.models.feedback;

import dto.models.FeedbackDTO;
import engine.enums.Rating;

import java.time.LocalDate;

public class Feedback {

    private final String username;
    private final Rating rating;
    private final String message;
    private final LocalDate orderDate;
    private final String storeName;

    public Feedback(String username, Rating rating, String message, LocalDate orderDate, String storeName) {
        this.username = username;
        this.rating = rating;
        this.message = message;
        this.orderDate = orderDate;
        this.storeName = storeName;
    }

    public FeedbackDTO toFeedbackDTO() {
        return new FeedbackDTO.Builder()
                .username(username)
                .rating(rating.getOrdinal())
                .message(message)
                .orderDate(String.format("%d/%d/%d", orderDate.getDayOfMonth(), orderDate.getMonthValue(), orderDate.getYear()))
                .storeName(storeName)
                .build();
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", message='" + message + '\'' +
                ", orderDate=" + orderDate +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
