package engine.models.item;

import dto.models.RegionItemDTO;
import engine.enums.PurchaseForm;
import engine.interfaces.Transferable;

public class RegionItem extends Item implements Transferable<RegionItemDTO> {

    private int amountOfStoresSelling;
    private float averagePrice;

    public RegionItem(int id, String name, PurchaseForm purchaseForm) {
        super(id, name, purchaseForm);
    }

    @Override
    public RegionItemDTO toDTO() {
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

    public void increaseAmountOfStoresSelling(float itemPrice) {
        amountOfStoresSelling++;
        averagePrice = (averagePrice * (amountOfStoresSelling - 1) + itemPrice) / amountOfStoresSelling;
    }

    public void decreaseAmountOfStoresSelling(float itemPrice) {
        amountOfStoresSelling--;
        averagePrice = (averagePrice * (amountOfStoresSelling + 1) - itemPrice) / amountOfStoresSelling;
    }

    public void updateAveragePrice(float oldItemPrice, float newItemPrice) {
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
