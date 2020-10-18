package dto.models;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class GeneralOrderDTO {

    private final int id;
    private final LocalDate orderDate;
    private final Point orderDestination;
    private final String customerUsername;
    private final Map<StoreDTO, SubOrderDTO> storeToOrder;
    private final List<OrderItemDTO> orderedItems;
    private final float totalItemsCost;
    private final float deliveryCost;
    private final float totalOrderCost;
    private final int totalItemsTypes;
    private final int totalItemsAmount;

    private GeneralOrderDTO(Builder builder) {
        this.id = builder.id;
        this.orderDate = builder.orderDate;
        this.customerUsername = builder.customerUsername;
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

    public float getDistanceFromUserByStore(StoreDTO store) {
        return storeToOrder.get(store).getDistanceFromUser();
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getCustomerUsername() {
        return customerUsername;
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
        return Collections.unmodifiableCollection(orderedItems);
    }

    public Collection<StoreDTO> getStores() {
        return Collections.unmodifiableCollection(storeToOrder.keySet());
    }

    public static final class Builder {

        private int id;
        private LocalDate orderDate;
        private Point orderDestination;
        private String customerUsername;
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

        public Builder customerUsername(String customerUsername) {
            this.customerUsername = customerUsername;
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
                ", customerUsername=" + customerUsername +
                ", storeToOrder=" + storeToOrder +
                ", orderedItems=" + orderedItems +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", totalOrderCost=" + totalOrderCost +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsAmount=" + totalItemsAmount +
                '}';
    }
}
