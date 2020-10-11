package dto.models;

import java.util.Objects;

public class StoreItemDTO {

    private final int id;
    private final String name;
    private final String purchaseForm;
    private final float purchaseAmount;
    private final int price;
    private final int sellingStoreId;

    private StoreItemDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.purchaseForm = builder.purchaseForm;
        this.purchaseAmount = builder.purchaseAmount;
        this.price = builder.price;
        this.sellingStoreId = builder.sellingStoreId;
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

    public int getPrice() {
        return price;
    }

    public static final class Builder {

        private int id;
        private String name;
        private String purchaseForm;
        private float purchaseAmount;
        private int price;
        private int sellingStoreId;

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

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder sellingStoreId(int sellingStoreId) {
            this.sellingStoreId = sellingStoreId;
            return this;
        }

        public StoreItemDTO build() {
            return new StoreItemDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreItemDTO that = (StoreItemDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StoreItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchaseForm='" + purchaseForm + '\'' +
                ", purchaseAmount=" + purchaseAmount +
                ", price=" + price +
                ", sellingStoreId=" + sellingStoreId +
                '}';
    }
}
