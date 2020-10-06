package engine.models.item;

import engine.enums.PurchaseForm;

import java.util.Objects;

public abstract class Item {

    protected final int id;
    protected final String name;
    protected final PurchaseForm purchaseForm;
    protected float purchaseAmount;

    public Item(int id, String name, PurchaseForm purchaseForm) {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PurchaseForm getPurchaseForm() {
        return purchaseForm;
    }

    public float getPurchaseAmount() {
        return purchaseAmount;
    }

    public void increasePurchaseAmount(float purchaseAmountToAdd) {
        purchaseAmount += purchaseAmountToAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm=" + purchaseForm +
                ", purchaseAmount=" + purchaseAmount +
                '}';
    }
}
