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

public class SDMSingleRegionManager implements SingleRegionManager {

    private final String regionName;
    private final String regionOwnerUsername;
    private final Map<Integer, RegionItem> itemIdToItem;
    private final Map<Integer, Store> storeIdToStore;
    private final Map<Integer, GeneralOrder> orderIdToOrder = new HashMap<>();
    private final Map<String, GeneralOrder> usernameToPendingOrder = new HashMap<>();
    private float averageOrderItemsCost;

    SDMSingleRegionManager(String regionName, String regionOwnerUsername, Map<Integer, Store> storeIdToStore, Map<Integer, RegionItem> itemIdToItem) {
        this.regionName = regionName;
        this.regionOwnerUsername = regionOwnerUsername;
        this.storeIdToStore = storeIdToStore;
        this.itemIdToItem = itemIdToItem;
        initializeItemsSellsInStores();
        initializeStoresOwner();
    }

    @Override
    public RegionDTO getRegionDetails() {
        return new RegionDTO.Builder()
                .name(regionName)
                .ownerUsername(regionOwnerUsername)
                .totalItems(itemIdToItem.size())
                .averageOrdersCost(averageOrderItemsCost)
                .totalStores(storeIdToStore.size())
                .totalOrdersMade(orderIdToOrder.size())
                .build();
    }

    @Override
    public StoreDTO getStoreById(int storeId) {
        if (!storeIdToStore.containsKey(storeId)) {
            throw new IdNotExistsException();
        }

        return storeIdToStore.get(storeId).toStoreDTO();
    }

    @Override
    public RegionItemDTO getItemById(int itemId) {
        if (!itemIdToItem.containsKey(itemId)) {
            throw new IdNotExistsException();
        }

        return itemIdToItem.get(itemId).toRegionItemDTO();
    }

    @Override
    public Collection<RegionItemDTO> getAllItemsInRegion() {
        return Collections.unmodifiableCollection(itemIdToItem.values()
                .stream()
                .map(RegionItem::toRegionItemDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<StoreDTO> getAllStoresInRegion() {
        return Collections.unmodifiableCollection(storeIdToStore.values()
                .stream()
                .map(Store::toStoreDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<GeneralOrderDTO> getAllOrdersInRegion() {
       return Collections.unmodifiableCollection(orderIdToOrder.values()
               .stream()
               .map(GeneralOrder::toGeneralOrderDTO)
               .collect(Collectors.toSet()));
    }

    @Override
    public void deleteItemFromStore(int storeId, int itemId) {
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
    public void addNewItemToStore(int storeId, int itemId, float itemPrice) {
        Store store = storeIdToStore.get(storeId);
        RegionItem itemToAdd = itemIdToItem.get(itemId);

        if (store.isItemSold(itemId)) {
            throw new ItemAlreadySoldByStoreException();
        }

        store.addNewItem(new StoreItem(itemToAdd.getId(), itemToAdd.getName(), itemToAdd.getPurchaseForm(), itemPrice, storeId));
        itemToAdd.increaseAmountOfStoresSelling(itemPrice);
    }

    @Override
    public void updateItemPriceInStore(int storeId, int itemId, float newItemPrice) {
        Store store = storeIdToStore.get(storeId);
        RegionItem itemToUpdate = itemIdToItem.get(itemId);

        if (!store.isItemSold(itemId)) {
            throw new ItemNotSoldByStoreException();
        }

        store.updateItemPrice(itemId, newItemPrice);
        itemToUpdate.updateAveragePrice(store.getItemById(itemId).getPrice(), newItemPrice);
    }

    @Override
    public void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore) {
        if (itemIdToItem.containsKey(itemId)) {
            throw new IllegalStateException("The item id '" + itemId + "' is already taken.");
        }

        RegionItem newItem = new RegionItem(itemId, itemName, PurchaseForm.valueOf(itemPurchaseForm.trim().toUpperCase()));
        itemIdToItem.put(newItem.getId(), newItem);
        storeIdToItemPriceInStore.forEach((storeId, itemPriceInStore) -> addNewItemToStore(storeId, itemId, itemPriceInStore));
    }

    @Override
    public void addNewStoreToRegion(String ownerUsername, int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore) {
        if (storeIdToStore.containsKey(storeId)) {
            throw new IllegalStateException("The store id '" + storeId + "' is already taken.");
        }

        if (isLocationOccupied(storeLocation)) {
            throw new IdenticalLocationsException(String.format("The location (%d, %d) is already taken.", storeLocation.x, storeLocation.y));
        }

        Store newStore = new Store(storeId, storeName, storeDeliveryPPK, new Location(storeLocation.x, storeLocation.y));
        itemIdToItemPriceInStore.forEach((itemId, itemPrice) -> addNewItemToStore(storeId, itemId, itemPrice));
        storeIdToStore.put(newStore.getId(), newStore);
        SDMUsersManager.getInstance().addNewStoreToStoreOwner(ownerUsername, newStore, regionName);

        // TODO: send notification to the region owner (only if the new store owner is not himself)
    }

    @Override
    public GeneralOrderDTO getPendingOrderByUsername(String username) {
        return usernameToPendingOrder.get(username).toGeneralOrderDTO();
    }

    @Override
    public void checkForValidOrderDestination(Point orderDestination) {
        if (isLocationOccupied(orderDestination)) {
            throw new IdenticalLocationsException("");
        }
    }

    @Override
    public void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, String customerUsername, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        List<OrderItemDTO> allOrderedItems = new LinkedList<>();
        Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems = new HashMap<>();
        Location destination = new Location(orderDestination.x, orderDestination.y);
        matchItemsWithStoresToOrderFrom(isDynamicOrder, allOrderedItems, storeToOrderedItems, chosenStore, itemToItemPurchaseAmount);
        GeneralOrder pendingOrder = new GeneralOrder(customerUsername, destination, orderDate, allOrderedItems, storeToOrderedItems);
        usernameToPendingOrder.put(customerUsername, pendingOrder);
    }

    @Override
    public void addPendingOrderToOrdersStockByUsername(String username) {
        GeneralOrder pendingOrder = usernameToPendingOrder.get(username);
        pendingOrder.generateOrderId();
        orderIdToOrder.put(pendingOrder.getId(), pendingOrder);
        usernameToPendingOrder.remove(username);
        updateItemsPurchaseAmountAfterNewOrder(pendingOrder);
        updateStoresOrdersAfterNewOrder(pendingOrder);
        updateAverageOrderItemsCost(pendingOrder);
        SDMUsersManager.getInstance().addNewOrderToCustomer(username, pendingOrder, regionName);

        // TODO: send notification about new order to all the store owners of the stores participated in the order
        // TODO: add new transactions to the customer and all the store owners of the stores participated in the order
    }

    @Override
    public Map<StoreDTO, Collection<DiscountInformationDTO>> getAvailableDiscountsFromPendingOrderByUsername(String username) {
        GeneralOrder pendingOrder = usernameToPendingOrder.get(username);
        Map<StoreDTO, Collection<DiscountInformationDTO>> storeToAvailableDiscounts = new HashMap<>();

        for (StoreDTO store : pendingOrder.getStores()) {
            List<OrderItemDTO> storeOrderedItems = pendingOrder.getOrderByStore(store).getOrderedItems();
            Collection<DiscountInformationDTO> discountsInStore = storeIdToStore.get(store.getId()).findAvailableDiscountsByPurchases(storeOrderedItems);
            storeToAvailableDiscounts.put(store, discountsInStore);
        }

        // TODO: need to check in the UI for submitting one discount for the same trigger
        return Collections.unmodifiableMap(storeToAvailableDiscounts);
    }

    @Override
    public void addFeedbacksToStoresFromOrder(String username, Map<StoreDTO, FeedbackDTO> storeToFeedback) {
        storeToFeedback.forEach((storeDTO, feedback) -> {
            Store store = storeIdToStore.get(storeDTO.getId());
            store.addNewFeedback(feedback);
        });

        // TODO: send notifications to the store owner about all the stores who got a feedback
    }

    @Override
    public void addChosenDiscountOffersToPendingOrderByUsername(String username, Map<StoreDTO, List<DiscountOfferDTO>> storeToDiscountOffers) {
        GeneralOrder pendingOrder = usernameToPendingOrder.get(username);
        Map<StoreDTO, List<OrderItemDTO>> storeToDiscountOfferItems = new HashMap<>();

        for (StoreDTO store : storeToDiscountOffers.keySet()) {
            List<OrderItemDTO> discountOfferItems = getItemsFromDiscountOffers(storeToDiscountOffers.get(store));
            storeToDiscountOfferItems.put(store, discountOfferItems);
        }

        pendingOrder.addItemsFromDiscountOffers(storeToDiscountOfferItems);
    }

    private void updateAverageOrderItemsCost(GeneralOrder newOrder) {
        averageOrderItemsCost = (averageOrderItemsCost * (orderIdToOrder.size() - 1) + newOrder.getTotalItemsCost()) / averageOrderItemsCost;
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
            itemToItemPurchaseAmount.forEach((item, purchaseAmount) -> {
                OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(chosenStore.getItemById(item.getId())).itemOrderPrice(chosenStore.getItemById(item.getId()).getPrice()).quantity(purchaseAmount).isFromDiscount(false).build();
                allOrderedItems.add(orderedItemToAdd);
                storeToOrderedItems.get(chosenStore).add(orderedItemToAdd);
            });
        }
    }

    private void findCheapestStoresToOrderFrom(List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        itemToItemPurchaseAmount.forEach((item, purchaseAmount) -> {
            StoreDTO cheapestStore = findCheapestStore(item);
            OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(cheapestStore.getItemById(item.getId())).itemOrderPrice(cheapestStore.getItemById(item.getId()).getPrice()).quantity(purchaseAmount).isFromDiscount(false).build();
            allOrderedItems.add(orderedItemToAdd);
            if (!storeToOrderedItems.containsKey(cheapestStore)) {
                storeToOrderedItems.put(cheapestStore, new LinkedList<>());
            }

            storeToOrderedItems.get(cheapestStore).add(orderedItemToAdd);
        });
    }

    private StoreDTO findCheapestStore(RegionItemDTO item) {
        float lowestPrice = Float.MAX_VALUE;
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
        storeIdToStore.values().forEach(store -> SDMUsersManager.getInstance().addNewStoreToStoreOwner(regionOwnerUsername, store, regionName));
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
        return "SDMSingleRegionManager{" +
                "regionName='" + regionName + '\'' +
                ", regionOwnerUsername='" + regionOwnerUsername + '\'' +
                ", itemIdToItem=" + itemIdToItem +
                ", storeIdToStore=" + storeIdToStore +
                ", averageOrderItemsCost=" + averageOrderItemsCost +
                ", orderIdToOrder=" + orderIdToOrder +
                ", usernameToPendingOrder=" + usernameToPendingOrder +
                '}';
    }
}
