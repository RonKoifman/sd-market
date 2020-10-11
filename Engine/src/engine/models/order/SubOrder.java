package engine.models.order;

import dto.models.*;
import engine.models.location.Location;

import java.time.LocalDate;
import java.util.*;

public class SubOrder extends Order {

    private final StoreDTO store;
    private final float distanceFromCustomer;

    public SubOrder(StoreDTO store, CustomerDTO customer, Location orderDestination, LocalDate orderDate, List<OrderItemDTO> orderedItems) {
        super(customer, orderDate, orderDestination, orderedItems);
        this.store = store;
        this.distanceFromCustomer = calculateDistanceFromCustomer();
        this.totalItemsCost = calculateTotalItemsCost();
        this.deliveryCost = calculateDeliveryCost();
        this.totalOrderCost = calculateTotalOrderCost();
        this.totalItemsTypes = calculateTotalItemsTypes();
        this.totalItemsAmount = calculateTotalItemsAmount();
    }

    public SubOrderDTO toSubOrderDTO() {
        return new SubOrderDTO.Builder()
                .id(id)
                .deliveryCost(deliveryCost)
                .distanceFromCustomer(distanceFromCustomer)
                .orderDate(orderDate)
                .orderDestination(orderDestination)
                .totalItemsAmount(totalItemsAmount)
                .totalItemsCost(totalItemsCost)
                .totalOrderCost(totalOrderCost)
                .totalItemsTypes(totalItemsTypes)
                .orderedItems(orderedItems)
                .build();
    }

    public void addItemsFromDiscountOffers(List<OrderItemDTO> discountItems) {
        orderedItems.addAll(discountItems);
        totalItemsCost = calculateTotalItemsCost();
        totalOrderCost = calculateTotalOrderCost();
        totalItemsTypes = calculateTotalItemsTypes();
        totalItemsAmount = calculateTotalItemsAmount();
    }

    @Override
    protected float calculateDeliveryCost() {
        return store.getDeliveryPPK() * distanceFromCustomer;
    }

    private float calculateDistanceFromCustomer() {
        return (float)orderDestination.distance(store.getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubOrder that = (SubOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SubOrder{" +
                "store=" + store +
                ", distanceFromCustomer=" + distanceFromCustomer +
                ", id=" + id +
                ", customer=" + customer +
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