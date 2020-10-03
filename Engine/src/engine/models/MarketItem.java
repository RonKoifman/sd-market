package engine.models;

import dto.models.MarketItemDTO;
import engine.enums.PurchaseForm;

class MarketItem extends Item {

    private int amountOfStoresSelling;
    private float averagePrice;

    public MarketItem(int id, String name, PurchaseForm purchaseForm) {
        super(id, name, purchaseForm);
    }

    public MarketItemDTO toMarketItemDTO() {
        return new MarketItemDTO.Builder()
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
        return "MarketItem{" +
                "amountOfStoresSelling=" + amountOfStoresSelling +
                ", averagePrice=" + averagePrice +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
