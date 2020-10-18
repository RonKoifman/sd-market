package dto.models;

public class FeedbackDTO {

    private final String username;
    private final int rating;
    private final String message;
    private final String orderDate;
    private final String storeName;

    private FeedbackDTO(Builder builder) {
        this.username = builder.username;
        this.rating = builder.rating;
        this.message = builder.message;
        this.orderDate = builder.orderDate;
        this.storeName = builder.storeName;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getMessage() {
        return message;
    }

    public String getStoreName() {
        return storeName;
    }

    public static final class Builder {

        private String username;
        private int rating;
        private String message;
        private String orderDate;
        private String storeName;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder orderDate(String orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder storeName(String storeName) {
            this.storeName = storeName;
            return this;
        }

        public FeedbackDTO build() {
            return new FeedbackDTO(this);
        }
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", message='" + message + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }
}
