package engine.models.notification;

public class NewOrderNotification extends Notification {

    private final int orderId;
    private final String username;
    private final String storeName;
    private final int totalItemsTypes;
    private final float totalItemsCost;
    private final float deliveryCost;

    public NewOrderNotification(String storeName, int orderId, String username, int totalItemsTypes, float totalItemsCost, float deliveryCost) {
        super("New Order From Your Store");
        this.storeName = storeName;
        this.orderId = orderId;
        this.username = username;
        this.totalItemsTypes = totalItemsTypes;
        this.totalItemsCost = totalItemsCost;
        this.deliveryCost = deliveryCost;
        this.message = buildMessage();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    private String buildMessage() {
        return String.format("A new order (id: '%d') has been placed from your store '%s'!" + System.lineSeparator() +
                "The user '%s' has ordered from you %d items types with a total cost of $%.2f and a delivery cost of $%.2f.",
                orderId, storeName, username, totalItemsTypes, totalItemsCost, deliveryCost);
    }

    @Override
    public String toString() {
        return "NewOrderNotification{" +
                "orderId=" + orderId +
                ", username='" + username + '\'' +
                ", storeName='" + storeName + '\'' +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
