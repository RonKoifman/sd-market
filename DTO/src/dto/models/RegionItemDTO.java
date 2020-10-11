package dto.models;

import java.util.Objects;

public class RegionItemDTO {

    private final int id;
    private final String name;
    private final String purchaseForm;
    private final float purchaseAmount;
    private final int amountOfStoresSelling;
    private final float averagePrice;

    private RegionItemDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.purchaseForm = builder.purchaseForm;
        this.purchaseAmount = builder.purchaseAmount;
        this.amountOfStoresSelling = builder.amountOfStoresSelling;
        this.averagePrice = builder.averagePrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurchaseForm() {
        return purchaseForm;
    }

    public float getPurchaseAmount() {
        return purchaseAmount;
    }

    public int getAmountOfStoresSelling() {
        return amountOfStoresSelling;
    }

    public float getAveragePrice() {
        return averagePrice;
    }

    public static final class Builder {

        private int id;
        private String name;
        private String purchaseForm;
        private float purchaseAmount;
        private int amountOfStoresSelling;
        private float averagePrice;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder purchaseForm(String purchaseForm) {
            this.purchaseForm = purchaseForm;
            return this;
        }

        public Builder purchaseAmount(float purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public Builder amountOfStoresSelling(int amountOfStoresSelling) {
            this.amountOfStoresSelling = amountOfStoresSelling;
            return this;
        }

        public Builder averagePrice(float averagePrice) {
            this.averagePrice = averagePrice;
            return this;
        }

        public RegionItemDTO build() {
            return new RegionItemDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionItemDTO that = (RegionItemDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RegionItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm='" + purchaseForm + '\'' +
                ", purchaseAmount=" + purchaseAmount +
                ", amountOfStoresSelling=" + amountOfStoresSelling +
                ", averagePrice=" + averagePrice +
                '}';
    }
}
