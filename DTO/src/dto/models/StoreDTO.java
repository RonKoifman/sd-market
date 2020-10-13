package dto.models;

import java.awt.*;
import java.util.*;

public class StoreDTO {

    private final int id;
    private final String name;
    private final String ownerUsername;
    private final int deliveryPPK;
    private final Point location;
    private final Map<Integer, StoreItemDTO> itemIdToItem;
    private final Collection<SubOrderDTO> ordersMade;
    private final float totalIncomeFromDeliveries;
    private final float totalIncomeFromItems;

    private StoreDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.ownerUsername = builder.ownerUsername;
        this.location = builder.location;
        this.deliveryPPK = builder.deliveryPPK;
        this.totalIncomeFromDeliveries = builder.totalIncomeFromDeliveries;
        this.totalIncomeFromItems = builder.totalIncomeFromItems;
        this.itemIdToItem = builder.itemIdToItem;
        this.ordersMade = builder.ordersMade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public Collection<StoreItemDTO> getItemsForSell() {
        return itemIdToItem.values();
    }

    public StoreItemDTO getItemById(int itemId) {
        return itemIdToItem.get(itemId);
    }

    public Collection<SubOrderDTO> getOrdersMade() {
        return ordersMade;
    }

    public int getDeliveryPPK() {
        return deliveryPPK;
    }

    public float getTotalIncomeFromDeliveries() {
        return totalIncomeFromDeliveries;
    }

    public float getTotalIncomeFromItems() {
        return totalIncomeFromItems;
    }

    public boolean isItemSold(int itemId) {
        return itemIdToItem.containsKey(itemId);
    }

    public static final class Builder {

        private int id;
        private String name;
        private String ownerUsername;
        private int deliveryPPK;
        private Point location;
        private Map<Integer, StoreItemDTO> itemIdToItem;
        private Collection<SubOrderDTO> ordersMade;
        private float totalIncomeFromDeliveries;
        private float totalIncomeFromItems;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ownerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public Builder deliveryPPK(int deliveryPPK) {
            this.deliveryPPK = deliveryPPK;
            return this;
        }

        public Builder location(Point location) {
            this.location = location;
            return this;
        }

        public Builder itemIdToItem(Map<Integer, StoreItemDTO> itemIdToItem) {
            this.itemIdToItem = itemIdToItem;
            return this;
        }

        public Builder ordersMade(Collection<SubOrderDTO> ordersMade) {
            this.ordersMade = ordersMade;
            return this;
        }

        public Builder totalIncomeFromDeliveries(float totalIncomeFromDeliveries) {
            this.totalIncomeFromDeliveries = totalIncomeFromDeliveries;
            return this;
        }

        public Builder totalIncomeFromItems(float totalIncomeFromItems) {
            this.totalIncomeFromItems = totalIncomeFromItems;
            return this;
        }

        public StoreDTO build() {
            return new StoreDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreDTO storeDTO = (StoreDTO) o;
        return id == storeDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StoreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", deliveryPPK=" + deliveryPPK +
                ", location=" + location +
                ", itemIdToItem=" + itemIdToItem +
                ", ordersMade=" + ordersMade +
                ", totalIncomeFromDeliveries=" + totalIncomeFromDeliveries +
                ", totalIncomeFromItems=" + totalIncomeFromItems +
                '}';
    }
}
