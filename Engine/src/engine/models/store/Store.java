package engine.models.store;

import java.util.*;
import java.util.stream.Collectors;

import dto.models.DiscountInformationDTO;
import dto.models.StoreDTO;
import engine.exceptions.DiscountOffersRemovedException;
import engine.interfaces.Locationable;
import engine.interfaces.Identifiable;
import engine.interfaces.Transferable;
import engine.models.discount.DiscountInformation;
import engine.models.discount.DiscountTrigger;
import engine.models.feedback.Feedback;
import engine.models.item.OrderItem;
import engine.models.item.StoreItem;
import engine.models.location.Location;
import engine.models.order.SubOrder;

public class Store implements Locationable, Identifiable, Transferable<StoreDTO> {

    private final int id;
    private final String name;
    private String ownerUsername;
    private final Location location;
    private final float deliveryPPK;
    private float totalIncomeFromDeliveries;
    private float totalIncomeFromItems;
    private final Map<Integer, StoreItem> itemIdToItem = new HashMap<>();
    private final Map<String, DiscountInformation> discountNameToDiscountInformation = new HashMap<>();
    private final Map<Integer, SubOrder> orderIdToOrder = new HashMap<>();
    private final List<Feedback> feedbacksReceived = new LinkedList<>();

    public Store(int id, String name, float deliveryPPK, Location location) {
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

    @Override
    public StoreDTO toDTO() {
        return new StoreDTO.Builder()
                .id(id)
                .name(name)
                .location(location)
                .deliveryPPK(deliveryPPK)
                .ownerUsername(ownerUsername)
                .totalIncomeFromDeliveries(totalIncomeFromDeliveries)
                .totalIncomeFromItems(totalIncomeFromItems)
                .items(itemIdToItem.values().stream().map(StoreItem::toDTO).collect(Collectors.toSet()))
                .ordersMade(orderIdToOrder.values().stream().map(SubOrder::toDTO).collect(Collectors.toSet()))
                .build();
    }

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public int getAmountOfItemsForSale() {
        return itemIdToItem.size();
    }

    public float getDeliveryPPK() {
        return deliveryPPK;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public boolean isItemSold(int itemId) {
        return itemIdToItem.containsKey(itemId);
    }

    public StoreItem getItemById(int itemId) {
        return itemIdToItem.get(itemId);
    }

    public void addNewOrder(SubOrder newOrder) {
        orderIdToOrder.put(newOrder.getId(), newOrder);
        totalIncomeFromDeliveries += newOrder.getDeliveryCost();
        totalIncomeFromItems += newOrder.getTotalItemsCost();
        increaseItemsPurchaseAmountFromStore(newOrder.getOrderedItems());
    }

    public void addNewFeedback(Feedback newFeedback) {
        feedbacksReceived.add(newFeedback);
    }

    public Collection<Feedback> getFeedbacksReceived() {
        return Collections.unmodifiableCollection(feedbacksReceived);
    }

    public Collection<SubOrder> getOrdersMade() {
        return Collections.unmodifiableCollection(orderIdToOrder.values());
    }

    public void addNewDiscount(DiscountInformation newDiscount) {
        discountNameToDiscountInformation.put(newDiscount.getName(), newDiscount);
    }

    public DiscountInformation getDiscountInformationByDiscountName(String discountName) {
        return discountNameToDiscountInformation.getOrDefault(discountName, null);
    }

    public void addNewItem(StoreItem newItem) {
        itemIdToItem.put(newItem.getId(), newItem);
    }

    public void updateItemPrice(int itemIdToUpdate, float newPrice) {
        itemIdToItem.get(itemIdToUpdate).setPrice(newPrice);
    }

    public void removeItem(int itemIdToDelete) {
        itemIdToItem.remove(itemIdToDelete);
        removeDiscountOffersAfterDeletingItem(itemIdToDelete);
    }

    public Collection<DiscountInformationDTO> findAvailableDiscountsByPurchases(List<OrderItem> orderedItems) {
        Collection<DiscountInformationDTO> availableDiscounts = new LinkedList<>();

        for (OrderItem orderedItem : orderedItems) {
            Map<DiscountInformationDTO, Integer> matchingDiscountToDiscountAmount = findMatchingDiscountsByOrderedItem(orderedItem);
            addMatchingDiscountsToAvailableDiscounts(availableDiscounts, matchingDiscountToDiscountAmount);
        }

        return Collections.unmodifiableCollection(availableDiscounts);
    }

    private Map<DiscountInformationDTO, Integer> findMatchingDiscountsByOrderedItem(OrderItem orderedItem) {
        Map<DiscountInformationDTO, Integer> matchingDiscountToDiscountAmount = new HashMap<>();

        for (DiscountInformation discountInformation : discountNameToDiscountInformation.values()) {
            DiscountTrigger discountTrigger = discountInformation.getDiscountTrigger();
            if (discountTrigger.getItem().getId() == orderedItem.getItem().getId() && discountTrigger.getQuantity() <= orderedItem.getQuantity()) {
                matchingDiscountToDiscountAmount.put(discountInformation.toDTO(), (int)Math.floor((orderedItem.getQuantity() / discountTrigger.getQuantity())));
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

    private void increaseItemsPurchaseAmountFromStore(List<OrderItem> orderedItems) {
       orderedItems.forEach(orderedItem -> orderedItem.getItem().increasePurchaseAmount(orderedItem.getQuantity()));
    }

    private void removeDiscountOffersAfterDeletingItem(int itemIdToDelete) {
        Collection<DiscountInformation> discountsToDelete = getDiscountsInformationByItemId(itemIdToDelete);

        if (!discountsToDelete.isEmpty()) {
            StringBuilder offersNames = new StringBuilder();
            discountsToDelete.forEach(discountInformation -> offersNames
                    .append(System.lineSeparator()).append("'")
                    .append(discountInformation.getName()).append("'"));
            discountsToDelete.stream().map(DiscountInformation::getName).forEach(discountNameToDiscountInformation::remove);

            throw new DiscountOffersRemovedException("Discount offers that have been removed after deleting the selected item:"
                    + offersNames);
        }
    }

    private Collection<DiscountInformation> getDiscountsInformationByItemId(int itemIdToDelete) {
        return discountNameToDiscountInformation.values()
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
                ", ownerUsername='" + ownerUsername + '\'' +
                ", location=" + location +
                ", deliveryPPK=" + deliveryPPK +
                ", totalIncomeFromDeliveries=" + totalIncomeFromDeliveries +
                ", totalIncomeFromItems=" + totalIncomeFromItems +
                ", itemIdToItem=" + itemIdToItem +
                ", discountNameToDiscountInformation=" + discountNameToDiscountInformation +
                ", orderIdToOrder=" + orderIdToOrder +
                ", feedbacksReceived=" + feedbacksReceived +
                '}';
    }
}
