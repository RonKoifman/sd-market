package engine.managers;

import dto.models.*;
import engine.enums.PurchaseForm;
import engine.exceptions.*;
import engine.models.item.RegionItem;
import engine.models.item.StoreItem;
import engine.models.location.Location;
import engine.models.order.GeneralOrder;
import engine.models.store.Store;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SDMRegionManager implements RegionManager {

    private final String regionName;
    private final String regionOwnerUsername;
    private final Map<Integer, Store> storeIdToStore;
    private final Map<Integer, RegionItem> itemIdToItem;
    private final Map<Integer, GeneralOrder> orderIdToOrder = new HashMap<>();
    private float averageOrdersCost;
    private GeneralOrder pendingOrder;

    SDMRegionManager(String regionName, String regionOwnerUsername, Map<Integer, Store> storeIdToStore, Map<Integer, RegionItem> itemIdToItem) {
        this.regionName = regionName;
        this.regionOwnerUsername = regionOwnerUsername;
        this.storeIdToStore = storeIdToStore;
        this.itemIdToItem = itemIdToItem;
        initializeItemsSellsInStores();
        initializeStoresOwner();
    }

    @Override
    public RegionDTO getRegionDTO() {
        return new RegionDTO.Builder()
                .name(regionName)
                .ownerUsername(regionOwnerUsername)
                .totalItems(itemIdToItem.size())
                .averageOrdersCost(averageOrdersCost)
                .totalStores(storeIdToStore.size())
                .totalOrdersMade(orderIdToOrder.size())
                .build();
    }

    @Override
    public synchronized StoreDTO getStoreById(int storeId) {
        if (!storeIdToStore.containsKey(storeId)) {
            throw new IdNotExistsException();
        }

        return storeIdToStore.get(storeId).toStoreDTO();
    }

    @Override
    public synchronized RegionItemDTO getItemById(int itemId) {
        if (!itemIdToItem.containsKey(itemId)) {
            throw new IdNotExistsException();
        }

        return itemIdToItem.get(itemId).toRegionItemDTO();
    }

    @Override
    public synchronized Collection<RegionItemDTO> getAllItemsInRegion() {
        return itemIdToItem.values()
                .stream()
                .map(RegionItem::toRegionItemDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized Collection<StoreDTO> getAllStoresInRegion() {
        return storeIdToStore.values()
                .stream()
                .map(Store::toStoreDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized Collection<GeneralOrderDTO> getAllOrdersInRegion() {
       return orderIdToOrder.values()
               .stream()
               .map(GeneralOrder::toGeneralOrderDTO)
               .collect(Collectors.toSet());
    }

    @Override
    public synchronized void deleteItemFromStore(int storeId, int itemId) {
        Store store = storeIdToStore.get(storeId);
        RegionItem itemToDelete = itemIdToItem.get(itemId);

        if (!store.isItemSold(itemId)) {
            throw new ItemNotSoldByStoreException();
        }

        if (itemToDelete.getAmountOfStoresSelling() == 1) {
            throw new InvalidRemoveOperationException("'" + store.getName() + "' is the only store that sells this item.");
        }

        if (store.getAmountOfItemsForSell() == 1) {
            throw new InvalidRemoveOperationException("This item is the only item that '" + store.getName() + "' store is selling.");
        }

        store.removeItem(itemToDelete.getId());
        itemToDelete.decreaseAmountOfStoresSelling(store.getItemById(itemId).getPrice());
    }

    @Override
    public synchronized void addNewItemToStore(int storeId, int itemId, int itemPrice) {
        Store store = storeIdToStore.get(storeId);
        RegionItem itemToAdd = itemIdToItem.get(itemId);

        if (store.isItemSold(itemId)) {
            throw new ItemAlreadySoldByStoreException();
        }

        store.addNewItem(new StoreItem(itemToAdd.getId(), itemToAdd.getName(), itemToAdd.getPurchaseForm(), itemPrice, storeId));
        itemToAdd.increaseAmountOfStoresSelling(itemPrice);
    }

    @Override
    public synchronized void updateItemPriceInStore(int storeId, int itemId, int newItemPrice) {
        Store store = storeIdToStore.get(storeId);
        RegionItem itemToUpdate = itemIdToItem.get(itemId);

        if (!store.isItemSold(itemId)) {
            throw new ItemNotSoldByStoreException();
        }

        store.updateItemPrice(itemId, newItemPrice);
        itemToUpdate.updateAveragePrice(store.getItemById(itemId).getPrice(), newItemPrice);
    }

    @Override
    public synchronized void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore) {
        if (itemIdToItem.containsKey(itemId)) {
            throw new IllegalStateException("The item id '" + itemId + "' is already taken.");
        }

        RegionItem newItem = new RegionItem(itemId, itemName, PurchaseForm.valueOf(itemPurchaseForm.trim().toUpperCase()));
        itemIdToItem.put(newItem.getId(), newItem);
        storeIdToItemPriceInStore.forEach((storeId, itemPriceInStore) -> addNewItemToStore(storeId, itemId, itemPriceInStore));
    }

    @Override
    public synchronized void addNewStoreToRegion(String ownerUsername, int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore) {
        if (storeIdToStore.containsKey(storeId)) {
            throw new IllegalStateException("The store id '" + storeId + "' is already taken.");
        }

        if (isLocationOccupied(storeLocation)) {
            throw new IdenticalLocationsException(String.format("The location (%d, %d) is already taken.", storeLocation.x, storeLocation.y));
        }

        Store newStore = new Store(storeId, storeName, storeDeliveryPPK, new Location(storeLocation.x, storeLocation.y));
        newStore.setOwnerUsername(ownerUsername);
        itemIdToItemPriceInStore.forEach((itemId, itemPrice) -> addNewItemToStore(storeId, itemId, itemPrice));
        storeIdToStore.put(newStore.getId(), newStore);
    }

    @Override
    public synchronized GeneralOrderDTO getPendingOrder() {
        return pendingOrder.toGeneralOrderDTO();
    }

    @Override
    public synchronized void checkForValidOrderDestination(Point orderDestination) {
        if (isLocationOccupied(orderDestination)) {
            throw new IdenticalLocationsException("");
        }
    }

    @Override
    public synchronized void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, CustomerDTO customer, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        List<OrderItemDTO> allOrderedItems = new LinkedList<>();
        Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems = new HashMap<>();
        Location destination = new Location(orderDestination.x, orderDestination.y);
        matchItemsWithStoresToOrderFrom(isDynamicOrder, allOrderedItems, storeToOrderedItems, chosenStore, itemToItemPurchaseAmount);
        pendingOrder = new GeneralOrder(customer, destination, orderDate, allOrderedItems, storeToOrderedItems);
    }

    @Override
    public synchronized void addPendingOrderToOrdersStock() {
        pendingOrder.generateOrderId();
        orderIdToOrder.put(pendingOrder.getId(), pendingOrder);
        //userIdToUser.get(pendingOrder.getCustomer().getId()).addNewOrder(pendingOrder);
        // TODO: add new order to customer...
        updateItemsPurchaseAmountAfterNewOrder(pendingOrder);
        updateStoresOrdersAfterNewOrder(pendingOrder);
        updateAverageOrdersCost(pendingOrder);
    }

    @Override
    public synchronized Map<StoreDTO, Collection<DiscountInformationDTO>> getAvailableDiscountsFromPendingOrder() {
        Map<StoreDTO, Collection<DiscountInformationDTO>> storeToAvailableDiscounts = new HashMap<>();

        for (StoreDTO store : pendingOrder.getStores()) {
            List<OrderItemDTO> storeOrderedItems = pendingOrder.getOrderByStore(store).getOrderedItems();
            Collection<DiscountInformationDTO> discountsInStore = storeIdToStore.get(store.getId()).findAvailableDiscountsByPurchases(storeOrderedItems);
            storeToAvailableDiscounts.put(store, discountsInStore);
        }

        // TODO: need to check in the UI for submitting one discount for the same trigger
        return storeToAvailableDiscounts;
    }

    @Override
    public synchronized void addFeedbacksToPendingOrder(Map<StoreDTO, List<FeedbackDTO>> storeToFeedbacks) {
        // TODO: implement adding feedbacks
    }

    @Override
    public synchronized void addChosenDiscountOffersToPendingOrder(Map<StoreDTO, List<DiscountOfferDTO>> storeToDiscountOffers) {
        Map<StoreDTO, List<OrderItemDTO>> storeToDiscountOfferItems = new HashMap<>();

        for (StoreDTO store : storeToDiscountOffers.keySet()) {
            List<OrderItemDTO> discountOfferItems = getItemsFromDiscountOffers(storeToDiscountOffers.get(store));
            storeToDiscountOfferItems.put(store, discountOfferItems);
        }

        pendingOrder.addItemsFromDiscountOffers(storeToDiscountOfferItems);
    }

    private void updateAverageOrdersCost(GeneralOrder newOrder) {
        averageOrdersCost = (averageOrdersCost * (orderIdToOrder.size() - 1) + newOrder.getTotalItemsCost()) / averageOrdersCost;
    }

    private List<OrderItemDTO> getItemsFromDiscountOffers(List<DiscountOfferDTO> discountOffers) {
        return discountOffers.stream()
                .map(discountOffer -> new OrderItemDTO.Builder().item(discountOffer.getItem()).itemOrderPrice(discountOffer.getItemOfferPrice()).quantity(discountOffer.getQuantity()).isFromDiscount(true).build())
                .collect(Collectors.toList());
    }

    private void updateStoresOrdersAfterNewOrder(GeneralOrder newOrder) {
        newOrder.getStores().forEach(store -> storeIdToStore.get(store.getId()).addNewOrder(newOrder.getOrderByStore(store)));
    }

    private void updateItemsPurchaseAmountAfterNewOrder(GeneralOrder newOrder) {
        newOrder.getOrderedItems().forEach(orderedItem -> itemIdToItem.get(orderedItem.getItem().getId()).increasePurchaseAmount(orderedItem.getQuantity()));
    }

    private void matchItemsWithStoresToOrderFrom(boolean isDynamicOrder, List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        if (isDynamicOrder) {
            findCheapestStoresToOrderFrom(allOrderedItems, storeToOrderedItems, itemToItemPurchaseAmount);
        } else {
            storeToOrderedItems.put(chosenStore, new LinkedList<>());
            for (Map.Entry<RegionItemDTO, Float> entry : itemToItemPurchaseAmount.entrySet()) {
                OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(chosenStore.getItemById(entry.getKey().getId())).itemOrderPrice(chosenStore.getItemById(entry.getKey().getId()).getPrice()).quantity(entry.getValue()).isFromDiscount(false).build();
                allOrderedItems.add(orderedItemToAdd);
                storeToOrderedItems.get(chosenStore).add(orderedItemToAdd);
            }
        }
    }

    private void findCheapestStoresToOrderFrom(List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        for (Map.Entry<RegionItemDTO, Float> entry : itemToItemPurchaseAmount.entrySet()) {
            StoreDTO cheapestStore = findCheapestStore(entry.getKey());
            OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(cheapestStore.getItemById(entry.getKey().getId())).itemOrderPrice(cheapestStore.getItemById(entry.getKey().getId()).getPrice()).quantity(entry.getValue()).isFromDiscount(false).build();
            allOrderedItems.add(orderedItemToAdd);
            if (!storeToOrderedItems.containsKey(cheapestStore)) {
                storeToOrderedItems.put(cheapestStore, new LinkedList<>());
            }

            storeToOrderedItems.get(cheapestStore).add(orderedItemToAdd);
        }
    }

    private StoreDTO findCheapestStore(RegionItemDTO item) {
        int lowestPrice = Integer.MAX_VALUE;
        Store cheapestStore = null;

        for (Store store : storeIdToStore.values()) {
            if (store.isItemSold(item.getId())) {
                lowestPrice = Math.min(lowestPrice, store.getItemById(item.getId()).getPrice());
                if (store.getItemById(item.getId()).getPrice() == lowestPrice) {
                    cheapestStore = store;
                }
            }
        }

        assert cheapestStore != null;
        return cheapestStore.toStoreDTO();
    }

    private void initializeStoresOwner() {
        storeIdToStore.values().forEach(store -> store.setOwnerUsername(regionOwnerUsername));
    }

    private void initializeItemsSellsInStores() {
        for (RegionItem item : itemIdToItem.values()) {
            for (Store store : storeIdToStore.values()) {
                if (store.isItemSold(item.getId())) {
                    item.increaseAmountOfStoresSelling(store.getItemById(item.getId()).getPrice());
                }
            }
        }
    }

    private boolean isLocationOccupied(Point location) {
        return storeIdToStore.values()
                .stream()
                .map(Store::getLocation)
                .anyMatch(currentLocation -> currentLocation.x == location.x && currentLocation.y == location.y);
    }

    @Override
    public String toString() {
        return "SDMRegionManager{" +
                "regionName='" + regionName + '\'' +
                ", regionOwnerUsername='" + regionOwnerUsername + '\'' +
                ", storeIdToStore=" + storeIdToStore +
                ", itemIdToItem=" + itemIdToItem +
                ", orderIdToOrder=" + orderIdToOrder +
                ", pendingOrder=" + pendingOrder +
                '}';
    }
}
