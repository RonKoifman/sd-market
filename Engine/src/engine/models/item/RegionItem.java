package engine.models.item;

import dto.models.RegionItemDTO;
import engine.enums.PurchaseForm;

public class RegionItem extends Item {

    private int amountOfStoresSelling;
    private float averagePrice;

    public RegionItem(int id, String name, PurchaseForm purchaseForm) {
        super(id, name, purchaseForm);
    }

    public RegionItemDTO toRegionItemDTO() {
        return new RegionItemDTO.Builder()
                .id(id)
                .name(name)
                .purchaseForm(purchaseForm.getValue())
                .purchaseAmount(purchaseAmount)
                .averagePrice(averagePrice)
                .amountOfStoresSelling(amountOfStoresSelling)
                .build();
    }

    public int getAmountOfStoresSelling() {
        return amountOfStoresSelling;
    }

    public float getAveragePrice() {
        return averagePrice;
    }

    public void increaseAmountOfStoresSelling(int itemPrice) {
        amountOfStoresSelling++;
        averagePrice = (averagePrice * (amountOfStoresSelling - 1) + itemPrice) / amountOfStoresSelling;
    }

    public void decreaseAmountOfStoresSelling(int itemPrice) {
        amountOfStoresSelling--;
        averagePrice = (averagePrice * (amountOfStoresSelling + 1) - itemPrice) / amountOfStoresSelling;
    }

    public void updateAveragePrice(int oldItemPrice, int newItemPrice) {
        averagePrice = (averagePrice * amountOfStoresSelling - oldItemPrice + newItemPrice) / amountOfStoresSelling;
    }

    @Override
    public String toString() {
        return "RegionItem{" +
                "amountOfStoresSelling=" + amountOfStoresSelling +
                ", averagePrice=" + averagePrice +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
