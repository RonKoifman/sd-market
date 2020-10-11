package engine.models.item;

import dto.models.StoreItemDTO;
import engine.enums.PurchaseForm;

public class StoreItem extends Item {

    private final int sellingStoreId;
    private int price;

    public StoreItem(int id, String name, PurchaseForm purchaseForm, int price, int sellingStoreId) {
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

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getSellingStoreId() {
        return sellingStoreId;
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
