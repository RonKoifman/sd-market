package engine.models.store;

import java.util.*;
import java.util.stream.Collectors;

import dto.models.DiscountInformationDTO;
import dto.models.OrderItemDTO;
import dto.models.StoreDTO;
import engine.exceptions.DiscountOffersRemovedException;
import engine.interfaces.Locationable;
import engine.interfaces.Identifiable;
import engine.models.zone.Zone;
import engine.models.discount.DiscountInformation;
import engine.models.discount.DiscountTrigger;
import engine.models.item.StoreItem;
import engine.models.location.Location;
import engine.models.order.SubOrder;

public class Store implements Locationable, Identifiable {

    private final int id;
    private final String name;
    private final int deliveryPPK;
    private final Location location;
    private final Map<Integer, StoreItem> itemIdToItem = new HashMap<>();
    private final List<DiscountInformation> discountsInformation = new LinkedList<>();
    private final Map<Integer, SubOrder> orderIdToOrder = new HashMap<>();
    private float totalPaymentForDeliveries;
    //private final Zone zone;

    public Store(int id, String name, int deliveryPPK, Location location) {
        this.id = id;
        this.name = name;
        this.deliveryPPK = deliveryPPK;
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDeliveryPPK() {
        return deliveryPPK;
    }

    public int getAmountOfItemsForSell() {
        return itemIdToItem.size();
    }

    public StoreDTO toStoreDTO() {
        return new StoreDTO.Builder()
                .id(id)
                .name(name)
                .location(location)
                .deliveryPPK(deliveryPPK)
                .totalPaymentForDeliveries(totalPaymentForDeliveries)
                .itemIdToItem(itemIdToItem.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toStoreItemDTO())))
                .ordersMade(orderIdToOrder.values().stream().map(SubOrder::toSubOrderDTO).collect(Collectors.toSet()))
                .discountsInformation(discountsInformation.stream().map(DiscountInformation::toDiscountInformationDTO).collect(Collectors.toList()))
                .build();
    }

    public boolean isItemSold(int itemId) {
        return itemIdToItem.containsKey(itemId);
    }

    public StoreItem getItemById(int itemId) {
        return itemIdToItem.get(itemId);
    }

    public void addNewOrder(SubOrder newOrder) {
        orderIdToOrder.put(newOrder.getId(), newOrder);
        increaseItemsPurchaseAmountFromStore(newOrder.getOrderedItems());
        increaseTotalPaymentForDeliveries(newOrder.getDeliveryCost());
    }

    public void addNewDiscount(DiscountInformation newDiscount) {
        discountsInformation.add(newDiscount);
    }

    public void addNewItem(StoreItem newItem) {
        itemIdToItem.put(newItem.getId(), newItem);
    }

    public void updateItemPrice(int itemIdToUpdate, int newPrice) {
        itemIdToItem.get(itemIdToUpdate).setPrice(newPrice);
    }

    public void removeItem(int itemIdToDelete) {
        itemIdToItem.remove(itemIdToDelete);
        removeDiscountOffersAfterDeletingItem(itemIdToDelete);
    }

    public Collection<DiscountInformationDTO> findAvailableDiscountsByPurchases(List<OrderItemDTO> orderedItems) {
        Collection<DiscountInformationDTO> availableDiscounts = new LinkedList<>();

        for (OrderItemDTO orderedItem : orderedItems) {
            Map<DiscountInformationDTO, Integer> matchingDiscountToDiscountAmount = findMatchingDiscountsByOrderedItem(orderedItem);
            addMatchingDiscountsToAvailableDiscounts(availableDiscounts, matchingDiscountToDiscountAmount);
        }

        return availableDiscounts;
    }

    private Map<DiscountInformationDTO, Integer> findMatchingDiscountsByOrderedItem(OrderItemDTO orderedItem) {
        Map<DiscountInformationDTO, Integer> matchingDiscountToDiscountAmount = new HashMap<>();

        for (DiscountInformation discountInformation : discountsInformation) {
            DiscountTrigger discountTrigger = discountInformation.getDiscountTrigger();
            if (discountTrigger.getItem().getId() == orderedItem.getItem().getId() && discountTrigger.getQuantity() <= orderedItem.getQuantity()) {
                matchingDiscountToDiscountAmount.put(discountInformation.toDiscountInformationDTO(), (int)Math.floor((orderedItem.getQuantity() / discountTrigger.getQuantity())));
            }
        }

        return matchingDiscountToDiscountAmount;
    }

    private void addMatchingDiscountsToAvailableDiscounts(Collection<DiscountInformationDTO> availableDiscounts, Map<DiscountInformationDTO, Integer> matchingDiscountToDiscountAmount) {
        for (DiscountInformationDTO discount : matchingDiscountToDiscountAmount.keySet()) {
            for (int i = 0; i < matchingDiscountToDiscountAmount.get(discount); i++) {
                availableDiscounts.add(discount);
            }
        }
    }

    private void increaseTotalPaymentForDeliveries(float paymentForDeliveryToAdd) {
        totalPaymentForDeliveries += paymentForDeliveryToAdd;
    }

    private void increaseItemsPurchaseAmountFromStore(List<OrderItemDTO> orderedItems) {
       orderedItems.forEach(orderedItem -> itemIdToItem.get(orderedItem.getItem().getId()).increasePurchaseAmount(orderedItem.getQuantity()));
    }

    private void removeDiscountOffersAfterDeletingItem(int itemIdToDelete) {
        Collection<DiscountInformation> discountsToDelete = getDiscountsInformationByItemId(itemIdToDelete);

        if (!discountsToDelete.isEmpty()) {
            StringBuilder offersNames = new StringBuilder();
            discountsToDelete.forEach(discountInformation -> offersNames
                    .append(System.lineSeparator()).append("'")
                    .append(discountInformation.getName()).append("'"));
            discountsToDelete.forEach(discountsInformation::remove);

            throw new DiscountOffersRemovedException("Discount offers that have been removed after deleting the selected item:"
                    + offersNames);
        }
    }

    private Collection<DiscountInformation> getDiscountsInformationByItemId(int itemIdToDelete) {
        return discountsInformation
                .stream()
                .filter(discountInformation -> discountInformation.getDiscountTrigger().getItem().getId() == itemIdToDelete || discountInformation.isItemPartOfAnOffer(itemIdToDelete))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deliveryPPK=" + deliveryPPK +
                ", location=" + location +
                ", itemIdToItem=" + itemIdToItem +
                ", discountsInformation=" + discountsInformation +
                ", orderIdToOrder=" + orderIdToOrder +
                ", totalPaymentForDeliveries=" + totalPaymentForDeliveries +
                '}';
    }
}
