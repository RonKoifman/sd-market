package dto.models;

import java.util.Objects;

public class RegionDTO {

    private final String name;
    private final String ownerUsername;
    private final int totalItems;
    private final int totalStores;
    private final int totalOrdersMade;
    private final float averageOrdersCost;

    private RegionDTO(Builder builder) {
        this.name = builder.name;
        this.ownerUsername = builder.ownerUsername;
        this.totalItems = builder.totalItems;
        this.totalStores = builder.totalStores;
        this.totalOrdersMade = builder.totalOrdersMade;
        this.averageOrdersCost = builder.averageOrdersCost;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalStores() {
        return totalStores;
    }

    public int getTotalOrdersMade() {
        return totalOrdersMade;
    }

    public float getAverageOrdersCost() {
        return averageOrdersCost;
    }

    public static final class Builder {

        private String name;
        private String ownerUsername;
        private int totalItems;
        private int totalStores;
        private int totalOrdersMade;
        private float averageOrdersCost;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ownerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public Builder totalItems(int totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public Builder totalStores(int totalStores) {
            this.totalStores = totalStores;
            return this;
        }

        public Builder totalOrdersMade(int totalOrdersMade) {
            this.totalOrdersMade = totalOrdersMade;
            return this;
        }

        public Builder averageOrdersCost(float averageOrdersCost) {
            this.averageOrdersCost = averageOrdersCost;
            return this;
        }

        public RegionDTO build() {
            return new RegionDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionDTO regionDTO = (RegionDTO) o;
        return Objects.equals(name, regionDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RegionDTO{" +
                "name='" + name + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", totalItems=" + totalItems +
                ", totalStores=" + totalStores +
                ", totalOrdersMade=" + totalOrdersMade +
                ", averageOrdersCost=" + averageOrdersCost +
                '}';
    }
}
