package engine.models.discount;

import dto.models.DiscountOfferDTO;
import engine.interfaces.Transferable;
import engine.models.item.StoreItem;

import java.util.Objects;

public class DiscountOffer implements Transferable<DiscountOfferDTO> {

    private final StoreItem item;
    private final float quantity;
    private final float itemOfferPrice;
    private final int storeId;

    public DiscountOffer(StoreItem item, float quantity, float itemOfferPrice, int storeId) {
        this.item = item;
        this.quantity = quantity;
        this.itemOfferPrice = itemOfferPrice;
        this.storeId = storeId;
    }

    @Override
    public DiscountOfferDTO toDTO() {
        return new DiscountOfferDTO.Builder()
                .item(item.toDTO())
                .quantity(quantity)
                .itemOfferPrice(itemOfferPrice)
                .storeId(storeId)
                .build();
    }

    public StoreItem getItem() {
        return item;
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
                ", storeId=" + storeId +
                '}';
    }
}
