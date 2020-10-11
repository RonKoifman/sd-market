package dto.models;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SubOrderDTO {

    private final int id;
    private final LocalDate orderDate;
    private final Point orderDestination;
    private final float distanceFromCustomer;
    private final int totalItemsAmount;
    private final int totalItemsTypes;
    private final float totalItemsCost;
    private final float deliveryCost;
    private final float totalOrderCost;
    private final List<OrderItemDTO> orderedItems;

    private SubOrderDTO(Builder builder) {
        this.id = builder.id;
        this.orderDate = builder.orderDate;
        this.orderDestination = builder.orderDestination;
        this.distanceFromCustomer = builder.distanceFromCustomer;
        this.totalItemsAmount = builder.totalItemsAmount;
        this.totalItemsCost = builder.totalItemsCost;
        this.deliveryCost = builder.deliveryCost;
        this.totalOrderCost = builder.totalOrderCost;
        this.totalItemsTypes = builder.totalItemsTypes;
        this.orderedItems = builder.orderedItems;
    }

    public int getId() {
        return id;
    }

    public Collection<OrderItemDTO> getDiscountOrderedItems() {
        return orderedItems.stream()
                .filter(OrderItemDTO::isFromDiscount)
                .collect(Collectors.toList());
    }

    public Collection<OrderItemDTO> getNonDiscountOrderedItems() {
        return orderedItems.stream()
                .filter(orderedItem -> !orderedItem.isFromDiscount())
                .collect(Collectors.toList());
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Point getOrderDestination() {
        return orderDestination;
    }

    public float getDistanceFromCustomer() {
        return distanceFromCustomer;
    }

    public int getTotalItemsAmount() {
        return totalItemsAmount;
    }

    public float getTotalItemsCost() {
        return totalItemsCost;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public float getTotalOrderCost() {
        return totalOrderCost;
    }

    public int getTotalItemsTypes() {
        return totalItemsTypes;
    }

    public static final class Builder {

        private int id;
        private LocalDate orderDate;
        private Point orderDestination;
        private float distanceFromCustomer;
        private int totalItemsTypes;
        private int totalItemsAmount;
        private float totalItemsCost;
        private float deliveryCost;
        private float totalOrderCost;
        private List<OrderItemDTO> orderedItems;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder orderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder orderDestination(Point orderDestination) {
            this.orderDestination = orderDestination;
            return this;
        }

        public Builder distanceFromCustomer(float distanceFromCustomer) {
            this.distanceFromCustomer = distanceFromCustomer;
            return this;
        }

        public Builder totalItemsAmount(int totalItemsAmount) {
            this.totalItemsAmount = totalItemsAmount;
            return this;
        }

        public Builder totalItemsCost(float totalItemsCost) {
            this.totalItemsCost = totalItemsCost;
            return this;
        }

        public Builder deliveryCost(float deliveryCost) {
            this.deliveryCost = deliveryCost;
            return this;
        }

        public Builder totalOrderCost(float totalOrderCost) {
            this.totalOrderCost = totalOrderCost;
            return this;
        }

        public Builder totalItemsTypes(int totalItemsTypes) {
            this.totalItemsTypes = totalItemsTypes;
            return this;
        }

        public Builder orderedItems(List<OrderItemDTO> orderedItems) {
            this.orderedItems = orderedItems;
            return this;
        }

        public SubOrderDTO build() {
            return new SubOrderDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubOrderDTO that = (SubOrderDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SubOrderDTO{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", orderDestination=" + orderDestination +
                ", distanceFromCustomer=" + distanceFromCustomer +
                ", totalItemsAmount=" + totalItemsAmount +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", totalOrderCost=" + totalOrderCost +
                ", orderedItems=" + orderedItems +
                '}';
    }
}
