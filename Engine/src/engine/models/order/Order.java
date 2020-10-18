package engine.models.order;

import dto.models.OrderItemDTO;
import engine.enums.PurchaseForm;
import engine.interfaces.Identifiable;
import engine.interfaces.Locationable;
import engine.models.location.Location;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Order implements Identifiable, Locationable, Serializable {

    protected int id;
    protected final String customerUsername;
    protected final Location orderDestination;
    protected final LocalDate orderDate;
    protected final List<OrderItemDTO> orderedItems = new LinkedList<>();
    protected float totalItemsCost;
    protected float deliveryCost;
    protected float totalOrderCost;
    protected int totalItemsTypes;
    protected int totalItemsAmount;
    private static int idGenerator;

    public Order(String customerUsername, LocalDate orderDate, Location orderDestination, List<OrderItemDTO> orderedItems) {
        this.customerUsername = customerUsername;
        this.orderDate = orderDate;
        this.orderDestination = orderDestination;
        this.orderedItems.addAll(orderedItems);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return orderDestination;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public List<OrderItemDTO> getOrderedItems() {
        return Collections.unmodifiableList(orderedItems);
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public float getTotalItemsCost() {
        return totalItemsCost;
    }

    protected void generateOrderId() {
        this.id = ++idGenerator;
    }

    protected void setOrderId(int id) {
        this.id = id;
    }

    protected float calculateTotalOrderCost() {
        return totalItemsCost + deliveryCost;
    }

    protected int calculateTotalItemsTypes() {
        return orderedItems.stream()
                .map(orderedItem -> orderedItem.getItem().getId())
                .collect(Collectors.toSet())
                .size();
    }

    protected int calculateTotalItemsAmount() {
        return Math.round(orderedItems.stream()
                .map(orderedItem -> PurchaseForm.valueOf(orderedItem.getItem().getPurchaseForm().toUpperCase()).equals(PurchaseForm.WEIGHT) ? 1.0f : orderedItem.getQuantity())
                .reduce(0.0f, Float::sum));
    }

    protected float calculateTotalItemsCost() {
        return orderedItems.stream()
                .map(orderedItem -> orderedItem.getQuantity() * orderedItem.getItemOrderPrice())
                .reduce(0.0f, Float::sum);
    }

    protected abstract float calculateDeliveryCost();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerUsername=" + customerUsername +
                ", orderDestination=" + orderDestination +
                ", orderDate=" + orderDate +
                ", orderedItems=" + orderedItems +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", totalOrderCost=" + totalOrderCost +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsAmount=" + totalItemsAmount +
                '}';
    }
}
