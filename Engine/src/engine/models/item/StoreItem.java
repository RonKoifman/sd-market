package engine.models.item;

import dto.models.StoreItemDTO;
import engine.enums.PurchaseForm;

import java.util.Objects;

public class StoreItem extends Item {

    private final int sellingStoreId;
    private float price;

    public StoreItem(int id, String name, PurchaseForm purchaseForm, float price, int sellingStoreId) {
        super(id, name, purchaseForm);
        this.price = price;
        this.sellingStoreId = sellingStoreId;
    }

    public StoreItemDTO toStoreItemDTO() {
        return new StoreItemDTO.Builder()
                .id(id)
                .name(name)
                .purchaseForm(purchaseForm.getValue())
                .purchaseAmount(purchaseAmount)
                .price(price)
                .sellingStoreId(sellingStoreId)
                .build();
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StoreItem storeItem = (StoreItem) o;
        return sellingStoreId == storeItem.sellingStoreId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sellingStoreId);
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "sellingStoreId=" + sellingStoreId +
                ", price=" + price +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
