package engine.models.notification;

public class NewOrderNotification extends Notification {

    private final int orderId;
    private final String customerUsername;
    private final int totalItemsTypes;
    private final float totalItemsCost;
    private final float deliveryCost;

    public NewOrderNotification(int orderId, String customerUsername, int totalItemsTypes, float totalItemsCost, float deliveryCost) {
        super("New Order From Your Store(s)");
        this.orderId = orderId;
        this.customerUsername = customerUsername;
        this.totalItemsTypes = totalItemsTypes;
        this.totalItemsCost = totalItemsCost;
        this.deliveryCost = deliveryCost;
        this.message = buildMessage();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public int getTotalItemsTypes() {
        return totalItemsTypes;
    }

    public float getTotalItemsCost() {
        return totalItemsCost;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    private String buildMessage() {
        return String.format("A new order (id: '%d') has been placed from store(s) you've owned." + System.lineSeparator() +
                "The customer '%s' has ordered from you %d items with a total cost of $%.2f and a delivery cost of $%.2f.",
                orderId, customerUsername, totalItemsTypes, totalItemsCost, deliveryCost);
    }

    @Override
    public String toString() {
        return "NewOrderNotification{" +
                "orderId=" + orderId +
                ", customerUsername='" + customerUsername + '\'' +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
