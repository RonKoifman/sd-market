package dto.models;

import java.util.Objects;

public class DiscountTriggerDTO {

    private final StoreItemDTO item;
    private final float quantity;

    private DiscountTriggerDTO(Builder builder) {
        this.item = builder.item;
        this.quantity = builder.quantity;
    }

    public StoreItemDTO getItemId() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    public static final class Builder {

        private StoreItemDTO item;
        private float quantity;

        public Builder item(StoreItemDTO item) {
            this.item = item;
            return this;
        }

        public Builder quantity(float quantity) {
            this.quantity = quantity;
            return this;
        }

        public DiscountTriggerDTO build() {
            return new DiscountTriggerDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountTriggerDTO that = (DiscountTriggerDTO) o;
        return Float.compare(that.quantity, quantity) == 0 &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity);
    }

    @Override
    public String toString() {
        return "DiscountTriggerDTO{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
