package dto.models;

import java.util.Objects;

public class OrderItemDTO {

    private final StoreItemDTO item;
    private final float quantity;
    private final float itemOrderPrice;
    private final boolean isFromDiscount;

    private OrderItemDTO(Builder builder) {
        this.item = builder.item;
        this.quantity = builder.quantity;
        this.itemOrderPrice = builder.itemOrderPrice;
        this.isFromDiscount = builder.isFromDiscount;
    }

    public StoreItemDTO getItem() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getItemOrderPrice() {
        return itemOrderPrice;
    }

    public boolean isFromDiscount() {
        return isFromDiscount;
    }

    public static final class Builder {

        private StoreItemDTO item;
        private float quantity;
        private float itemOrderPrice;
        private boolean isFromDiscount;

        public Builder item(StoreItemDTO item) {
            this.item = item;
            return this;
        }

        public Builder quantity(float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder itemOrderPrice(float itemOrderPrice) {
            this.itemOrderPrice = itemOrderPrice;
            return this;
        }

        public Builder isFromDiscount(boolean isFromDiscount) {
            this.isFromDiscount = isFromDiscount;
            return this;
        }

        public OrderItemDTO build() {
            return new OrderItemDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDTO that = (OrderItemDTO) o;
        return Float.compare(that.quantity, quantity) == 0 &&
                itemOrderPrice == that.itemOrderPrice &&
                isFromDiscount == that.isFromDiscount &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity, itemOrderPrice, isFromDiscount);
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", itemOrderPrice=" + itemOrderPrice +
                ", isFromDiscount=" + isFromDiscount +
                '}';
    }
}
