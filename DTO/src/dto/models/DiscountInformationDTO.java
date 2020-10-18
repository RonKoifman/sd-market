package dto.models;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DiscountInformationDTO {

    private final String name;
    private final DiscountTriggerDTO discountTrigger;
    private final String discountType;
    private final List<DiscountOfferDTO> discountOffers;

    private DiscountInformationDTO(Builder builder) {
        this.name = builder.name;
        this.discountTrigger = builder.discountTrigger;
        this.discountType = builder.discountType;
        this.discountOffers = builder.discountOffers;
    }

    public String getName() {
        return name;
    }

    public DiscountTriggerDTO getDiscountTrigger() {
        return discountTrigger;
    }

    public String getDiscountType() {
        return discountType;
    }

    public List<DiscountOfferDTO> getDiscountOffers() {
        return Collections.unmodifiableList(discountOffers);
    }

    public static final class Builder {

        private String name;
        private DiscountTriggerDTO discountTrigger;
        private String discountType;
        private List<DiscountOfferDTO> discountOffers;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder discountTrigger(DiscountTriggerDTO discountTrigger) {
            this.discountTrigger = discountTrigger;
            return this;
        }

        public Builder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public Builder discountOffers(List<DiscountOfferDTO> discountOffers) {
            this.discountOffers = discountOffers;
            return this;
        }

        public DiscountInformationDTO build() {
            return new DiscountInformationDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountInformationDTO that = (DiscountInformationDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "DiscountInformationDTO{" +
                "name='" + name + '\'' +
                ", discountTrigger=" + discountTrigger +
                ", discountType='" + discountType + '\'' +
                ", discountOffers=" + discountOffers +
                '}';
    }
}
