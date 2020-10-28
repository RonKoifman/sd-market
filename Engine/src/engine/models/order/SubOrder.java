package engine.models.order;

import dto.models.*;
import engine.interfaces.Transferable;
import engine.models.item.OrderItem;
import engine.models.location.Location;
import engine.models.store.Store;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SubOrder extends Order implements Transferable<SubOrderDTO> {

    private final Store store;
    private final float distanceFromCustomer;

    public SubOrder(Store store, String customerUsername, Location orderDestination, LocalDate orderDate, List<OrderItem> orderedItems) {
        super(customerUsername, orderDate, orderDestination, orderedItems);
        this.store = store;
        this.distanceFromCustomer = calculateDistanceFromCustomer();
        this.totalItemsCost = calculateTotalItemsCost();
        this.deliveryCost = calculateDeliveryCost();
        this.totalOrderCost = calculateTotalOrderCost();
        this.totalItemsTypes = calculateTotalItemsTypes();
        this.totalItemsAmount = calculateTotalItemsAmount();
    }

    @Override
    public SubOrderDTO toDTO() {
        return new SubOrderDTO.Builder()
                .id(id)
                .deliveryCost(deliveryCost)
                .distanceFromCustomer(distanceFromCustomer)
                .customerUsername(customerUsername)
                .orderDate(String.format("%d/%d/%d", orderDate.getDayOfMonth(), orderDate.getMonthValue(), orderDate.getYear()))
                .storeId(store.getId())
                .orderDestination(orderDestination)
                .totalItemsAmount(totalItemsAmount)
                .totalItemsCost(totalItemsCost)
                .totalOrderCost(totalOrderCost)
                .totalItemsTypes(totalItemsTypes)
                .orderedItems(orderedItems.stream().map(OrderItem::toDTO).collect(Collectors.toList()))
                .build();
    }

    public void addItemsFromDiscountOffers(List<OrderItem> discountItems) {
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
                ", distanceFromUser=" + distanceFromCustomer +
                ", id=" + id +
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