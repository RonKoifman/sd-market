package engine.models.feedback;

import dto.models.FeedbackDTO;
import engine.enums.Rating;

import java.time.LocalDate;

public class Feedback {

    private final String username;
    private final Rating rating;
    private final String feedbackText;
    private final LocalDate orderDate;
    private final String storeName;
    private final int storeId;

    public Feedback(String username, Rating rating, String feedbackText, LocalDate orderDate, String storeName, int storeId) {
        this.username = username;
        this.rating = rating;
        this.feedbackText = feedbackText;
        this.orderDate = orderDate;
        this.storeName = storeName;
        this.storeId = storeId;
    }

    public FeedbackDTO toFeedbackDTO() {
        return new FeedbackDTO.Builder()
                .username(username)
                .rating(rating.getOrdinal())
                .feedbackText(feedbackText)
                .orderDate(String.format("%d/%d/%d", orderDate.getDayOfMonth(), orderDate.getMonthValue(), orderDate.getYear()))
                .storeName(storeName)
                .build();
    }

    public String getUsername() {
        return username;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", feedbackText='" + feedbackText + '\'' +
                ", orderDate=" + orderDate +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
