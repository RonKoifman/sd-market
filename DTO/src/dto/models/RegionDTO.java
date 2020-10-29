package dto.models;

import java.util.Objects;

public class RegionDTO {

    private final String regionName;
    private final String ownerUsername;
    private final int totalItems;
    private final int totalStores;
    private final int totalOrdersMade;
    private final float averageOrderItemsCost;

    private RegionDTO(Builder builder) {
        this.regionName = builder.regionName;
        this.ownerUsername = builder.ownerUsername;
        this.totalItems = builder.totalItems;
        this.totalStores = builder.totalStores;
        this.totalOrdersMade = builder.totalOrdersMade;
        this.averageOrderItemsCost = builder.averageOrderItemsCost;
    }

    public String getRegionName() {
        return regionName;
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

    public float getAverageOrderItemsCost() {
        return averageOrderItemsCost;
    }

    public static final class Builder {

        private String regionName;
        private String ownerUsername;
        private int totalItems;
        private int totalStores;
        private int totalOrdersMade;
        private float averageOrderItemsCost;

        public Builder regionName(String regionName) {
            this.regionName = regionName;
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
            this.averageOrderItemsCost = averageOrdersCost;
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
        return Objects.equals(regionName, regionDTO.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionName);
    }

    @Override
    public String toString() {
        return "RegionDTO{" +
                "regionName='" + regionName + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", totalItems=" + totalItems +
                ", totalStores=" + totalStores +
                ", totalOrdersMade=" + totalOrdersMade +
                ", averageOrderItemsCost=" + averageOrderItemsCost +
                '}';
    }
}
