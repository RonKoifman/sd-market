package engine.models.item;

import dto.models.RegionItemDTO;
import engine.enums.PurchaseForm;
import engine.interfaces.Transferable;

public class RegionItem extends Item implements Transferable<RegionItemDTO> {

    private int amountOfStoresSelling;
    private float averagePrice;
    private final String belongedRegionName;

    public RegionItem(int id, String name, PurchaseForm purchaseForm, String belongedRegionName) {
        super(id, name, purchaseForm);
        this.belongedRegionName = belongedRegionName;
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

    public String getBelongedRegionName() {
        return belongedRegionName;
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
                ", belongedRegionName='" + belongedRegionName + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
