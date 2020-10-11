package dto.models;

import java.awt.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneralOrderDTO {

    private final int id;
    private final LocalDate orderDate;
    private final Point orderDestination;
    private final List<OrderItemDTO> orderedItems;
    private final Map<StoreDTO, SubOrderDTO> storeToOrder;
    private final float totalItemsCost;
    private final float deliveryCost;
    private final float totalOrderCost;
    private final int totalItemsTypes;
    private final int totalItemsAmount;

    private GeneralOrderDTO(Builder builder) {
        this.id = builder.id;
        this.orderDate = builder.orderDate;
        this.orderDestination = builder.orderDestination;
        this.orderedItems = builder.orderedItems;
        this.storeToOrder = builder.storeToOrder;
        this.totalItemsCost = builder.totalItemsCost;
        this.deliveryCost = builder.deliveryCost;
        this.totalOrderCost = builder.totalOrderCost;
        this.totalItemsTypes = builder.totalItemsTypes;
        this.totalItemsAmount = builder.totalItemsAmount;
    }

    public int getId() {
        return id;
    }

    public SubOrderDTO getOrderByStore(StoreDTO store) {
        return storeToOrder.get(store);
    }

    public float getDistanceFromCustomerByStore(StoreDTO store) {
        return storeToOrder.get(store).getDistanceFromCustomer();
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Point getOrderDestination() {
        return orderDestination;
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

    public int getTotalItemsAmount() {
        return totalItemsAmount;
    }

    public Collection<OrderItemDTO> getOrderedItems() {
        return orderedItems;
    }

    public Collection<StoreDTO> getStores() {
        return storeToOrder.keySet();
    }

    public static final class Builder {

        private int id;
        private LocalDate orderDate;
        private Point orderDestination;
        private List<OrderItemDTO> orderedItems;
        private Map<StoreDTO, SubOrderDTO> storeToOrder;
        private float totalItemsCost;
        private float deliveryCost;
        private float totalOrderCost;
        private int totalItemsTypes;
        private int totalItemsAmount;

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

        public Builder orderedItems(List <OrderItemDTO> orderedItems) {
            this.orderedItems = orderedItems;
            return this;
        }

        public Builder storeToOrder(Map<StoreDTO, SubOrderDTO> storeToOrder) {
            this.storeToOrder = storeToOrder;
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

        public Builder totalItemsAmount(int totalItemsAmount) {
            this.totalItemsAmount = totalItemsAmount;
            return this;
        }

        public GeneralOrderDTO build() {
            return new GeneralOrderDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralOrderDTO that = (GeneralOrderDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GeneralOrderDTO{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", orderDestination=" + orderDestination +
                ", orderedItems=" + orderedItems +
                ", storeToOrder=" + storeToOrder +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", totalOrderCost=" + totalOrderCost +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsAmount=" + totalItemsAmount +
                '}';
    }
}
