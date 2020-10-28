package engine.models.discount;

import dto.models.DiscountInformationDTO;
import engine.enums.DiscountOfferType;
import engine.interfaces.Transferable;

import java.util.*;
import java.util.stream.Collectors;

public class DiscountInformation implements Transferable<DiscountInformationDTO> {

    private final String name;
    private final DiscountTrigger discountTrigger;
    private final DiscountOfferType discountType;
    private final List<DiscountOffer> discountOffers = new LinkedList<>();

    public DiscountInformation(String name, DiscountTrigger discountTrigger, DiscountOfferType discountType) {
        this.name = name;
        this.discountTrigger = discountTrigger;
        this.discountType = discountType;
    }

    @Override
    public DiscountInformationDTO toDTO() {
        return new DiscountInformationDTO.Builder()
                .name(name)
                .discountTrigger(discountTrigger.toDTO())
                .discountType(discountType.getValue())
                .discountOffers(discountOffers.stream().map(DiscountOffer::toDTO).collect(Collectors.toList()))
                .build();
    }

    public DiscountTrigger getDiscountTrigger() {
        return discountTrigger;
    }

    public String getName() {
        return name;
    }

    public void addNewOffer(DiscountOffer newOffer) {
        discountOffers.add(newOffer);
    }

    public boolean isItemPartOfAnOffer(int itemId) {
        return discountOffers.stream()
                .anyMatch(discountOffer -> discountOffer.getItem().getId() == itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountInformation that = (DiscountInformation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "DiscountDetails{" +
                "name='" + name + '\'' +
                ", discountTrigger=" + discountTrigger +
                ", discountType=" + discountType +
                ", discountOffers=" + discountOffers +
                '}';
    }
}
