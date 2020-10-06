package engine.models.item;

import dto.models.StoreItemDTO;
import engine.enums.PurchaseForm;

public class StoreItem extends Item {

    private int price;

    public StoreItem(int id, String name, PurchaseForm purchaseForm, int price) {
        super(id, name, purchaseForm);
        this.price = price;
    }

    public StoreItemDTO toStoreItemDTO() {
        return new StoreItemDTO.Builder()
                .id(id)
                .name(name)
                .purchaseForm(purchaseForm.getValue())
                .purchaseAmount(purchaseAmount)
                .price(price)
                .build();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                ", price=" + price +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
