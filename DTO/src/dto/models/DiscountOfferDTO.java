package dto.models;

import java.util.Objects;

public class DiscountOfferDTO {

    private final StoreItemDTO item;
    private final float quantity;
    private final float itemOfferPrice;

    private DiscountOfferDTO(Builder builder) {
        this.item = builder.item;
        this.quantity = builder.quantity;
        this.itemOfferPrice = builder.itemOfferPrice;
    }

    public StoreItemDTO getItem() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getItemOfferPrice() {
        return itemOfferPrice;
    }

    public static final class Builder {

        private StoreItemDTO item;
        private float quantity;
        private float itemOfferPrice;

        public Builder item(StoreItemDTO item) {
            this.item= item;
            return this;
        }

        public Builder quantity(float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder itemOfferPrice(float itemOfferPrice) {
            this.itemOfferPrice = itemOfferPrice;
            return this;
        }

        public DiscountOfferDTO build() {
            return new DiscountOfferDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountOfferDTO that = (DiscountOfferDTO) o;
        return Float.compare(that.quantity, quantity) == 0 &&
                itemOfferPrice == that.itemOfferPrice &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity, itemOfferPrice);
    }

    @Override
    public String toString() {
        return "DiscountOfferDTO{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", itemOfferPrice=" + itemOfferPrice +
                '}';
    }
}
