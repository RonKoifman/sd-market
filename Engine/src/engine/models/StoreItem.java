package engine.models;

import dto.models.StoreItemDTO;
import engine.enums.PurchaseForm;

class StoreItem extends Item {

    private final Store storeSelling;
    private int price;

    public StoreItem(int id, String name, PurchaseForm purchaseForm, Store storeSelling, int price) {
        super(id, name, purchaseForm);
        this.storeSelling = storeSelling;
        this.price = price;
    }

    StoreItemDTO toStoreItemDTO() {
        return new StoreItemDTO.Builder()
                .id(id)
                .name(name)
                .purchaseForm(purchaseForm.getValue())
                .purchaseAmount(purchaseAmount)
                .price(price)
                .storeSelling(storeSelling.toStoreDTO())
                .build();
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Store getStoreSelling() {
        return storeSelling;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "storeSelling=" + storeSelling +
                ", price=" + price +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
