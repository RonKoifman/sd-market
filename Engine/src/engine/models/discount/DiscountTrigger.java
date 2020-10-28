package engine.models.discount;

import dto.models.DiscountTriggerDTO;
import engine.interfaces.Transferable;
import engine.models.item.StoreItem;

import java.util.Objects;

public class DiscountTrigger implements Transferable<DiscountTriggerDTO> {

    private final StoreItem item;
    private final float quantity;

    public DiscountTrigger(StoreItem item, float quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public DiscountTriggerDTO toDTO() {
        return new DiscountTriggerDTO.Builder()
                .item(item.toDTO())
                .quantity(quantity)
                .build();
    }

    public StoreItem getItem() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountTrigger that = (DiscountTrigger) o;
        return Float.compare(that.quantity, quantity) == 0 &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, quantity);
    }

    @Override
    public String toString() {
        return "DiscountTrigger{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
