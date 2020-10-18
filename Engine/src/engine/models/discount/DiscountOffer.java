package engine.models.discount;

import dto.models.DiscountOfferDTO;
import engine.models.item.StoreItem;

import java.util.Objects;

public class DiscountOffer {

    private final StoreItem item;
    private final float quantity;
    private final float itemOfferPrice;

    public DiscountOffer(StoreItem item, float quantity, float itemOfferPrice) {
        this.item = item;
        this.quantity = quantity;
        this.itemOfferPrice = itemOfferPrice;
    }

    DiscountOfferDTO toDiscountOfferDTO() {
        return new DiscountOfferDTO.Builder()
                .item(item.toStoreItemDTO())
                .quantity(quantity)
                .itemOfferPrice(itemOfferPrice)
                .build();
    }

    public StoreItem getItem() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getItemOfferPrice() {
        return itemOfferPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountOffer that = (DiscountOffer) o;
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
        return "DiscountOffer{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", itemOfferPrice=" + itemOfferPrice +
                '}';
    }
}
