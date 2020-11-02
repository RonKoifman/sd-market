package engine.managers;

import dto.models.*;
import engine.enums.PurchaseForm;
import engine.enums.Rating;
import engine.enums.TransactionType;
import engine.exceptions.*;
import engine.models.feedback.Feedback;
import engine.models.item.OrderItem;
import engine.models.item.RegionItem;
import engine.models.item.StoreItem;
import engine.models.location.Location;
import engine.models.notification.NewCompetitorNotification;
import engine.models.notification.NewFeedbackNotification;
import engine.models.notification.NewOrderNotification;
import engine.models.notification.Notification;
import engine.models.order.GeneralOrder;
import engine.models.order.SubOrder;
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
    public RegionDTO toDTO() {
        return new RegionDTO.Builder()
                .regionName(regionName)
                .ownerUsername(regionOwnerUsername)
                .totalItems(itemIdToItem.size())
                .averageOrdersCost(averageOrderItemsCost)
                .totalStores(storeIdToStore.size())
                .totalOrdersMade(orderIdToOrder.size())
                .build();
    }

    @Override
    public String getRegionOwnerUsername() {
        return regionOwnerUsername;
    }

    @Override
    public String getRegionName() {
        return regionName;
    }

    @Override
    public StoreDTO getStoreById(int storeId) {
        if (!storeIdToStore.containsKey(storeId)) {
            throw new IdNotExistsException();
        }

        return storeIdToStore.get(storeId).toDTO();
    }

    @Override
    public RegionItemDTO getItemById(int itemId) {
        if (!itemIdToItem.containsKey(itemId)) {
            throw new IdNotExistsException();
        }

        return itemIdToItem.get(itemId).toDTO();
    }

    @Override
    public Collection<RegionItemDTO> getAllItemsInRegion() {
        return Collections.unmodifiableCollection(itemIdToItem.values()
                .stream()
                .map(RegionItem::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<StoreDTO> getAllStoresInRegion() {
        return Collections.unmodifiableCollection(storeIdToStore.values()
                .stream()
                .map(Store::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<GeneralOrderDTO> getAllOrdersInRegion() {
       return Collections.unmodifiableCollection(orderIdToOrder.values()
               .stream()
               .map(GeneralOrder::toDTO)
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

        if (store.getAmountOfItemsForSale() == 1) {
            throw new InvalidRemoveOperationException("This item is the only item that '" + store.getName() + "' store is selling.");
        }

        itemToDelete.decreaseAmountOfStoresSelling(store.getItemById(itemId).getPrice());
        store.removeItem(itemToDelete.getId());
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
    public void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Float> storeIdToItemPriceInStore) {
        if (itemIdToItem.containsKey(itemId)) {
            throw new TakenIdException("The item ID '" + itemId + "' is already taken.");
        }

        RegionItem newItem = new RegionItem(itemId, itemName, PurchaseForm.valueOf(itemPurchaseForm.trim().toUpperCase()), regionName);
        itemIdToItem.put(newItem.getId(), newItem);
        storeIdToItemPriceInStore.forEach((storeId, itemPriceInStore) -> addNewItemToStore(storeId, itemId, itemPriceInStore));
    }

    @Override
    public void addNewStoreToRegion(int storeId, String ownerUsername, String storeName, Point storeLocation, float storeDeliveryPPK, Map<Integer, Float> itemIdToItemPriceInStore) {
        if (storeIdToStore.containsKey(storeId)) {
            throw new TakenIdException("The store ID '" + storeId + "' is already taken.");
        }

        Store newStore = new Store(storeId, storeName, storeDeliveryPPK, new Location(storeLocation.x, storeLocation.y), regionName);
        storeIdToStore.put(newStore.getId(), newStore);
        itemIdToItemPriceInStore.forEach((itemId, itemPrice) -> addNewItemToStore(storeId, itemId, itemPrice));
        SDMUsersManager.getInstance().addNewStoreToStoreOwner(ownerUsername, newStore, regionName);

        if (!ownerUsername.equals(regionOwnerUsername)) {
            Notification newNotification = new NewCompetitorNotification(regionName, ownerUsername, storeName, storeLocation, itemIdToItemPriceInStore.size(), itemIdToItem.size());
            SDMNotificationsManager.getInstance().addNewNotificationToUser(regionOwnerUsername, newNotification);
        }
    }

    @Override
    public void checkForAvailableId(int id, String objectType) {
        final String STORE =  "Store";
        final String REGION_ITEM = "RegionItem";

        switch (objectType) {
            case STORE: {
                if (storeIdToStore.containsKey(id)) {
                    throw new TakenIdException("The store ID '" + id + "' is already taken.");
                }
                break;
            }

            case REGION_ITEM: {
                if (itemIdToItem.containsKey(id)) {
                    throw new TakenIdException("The item ID '" + id + "' is already taken.");
                }
                break;
            }

            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public GeneralOrderDTO getPendingOrderByUsername(String username) {
        return usernameToPendingOrder.get(username).toDTO();
    }

    @Override
    public void checkForFreeLocation(Point location) {
        if (isLocationOccupied(location)) {
            throw new OccupiedLocationException(String.format("The location (%d, %d) is already occupied.", location.x, location.y));
        }
    }

    @Override
    public void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, String customerUsername, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        List<OrderItem> allOrderedItems = new LinkedList<>();
        Map<Store, List<OrderItem>> storeToOrderedItems = new HashMap<>();
        Location destination = new Location(orderDestination.x, orderDestination.y);
        Store store = chosenStore != null ? storeIdToStore.get(chosenStore.getId()) : null;

        matchItemsWithStoresToOrderFrom(isDynamicOrder, allOrderedItems, storeToOrderedItems, store, itemToItemPurchaseAmount);
        GeneralOrder pendingOrder = new GeneralOrder(customerUsername, destination, orderDate, allOrderedItems, storeToOrderedItems);
        usernameToPendingOrder.put(customerUsername, pendingOrder);
    }

    @Override
    public void confirmPendingOrderByUsername(String username) {
        GeneralOrder newOrder = usernameToPendingOrder.get(username);

        newOrder.generateOrderId();
        orderIdToOrder.put(newOrder.getId(), newOrder);
        updateRegionItemsPurchaseAmountAfterNewOrder(newOrder);
        updateStoresOrdersAfterNewOrder(newOrder);
        updateAverageOrderItemsCost(newOrder);

        SDMUsersManager.getInstance().addNewOrderToCustomer(username, newOrder, regionName);
        Map<String, Float> ownerUsernameToPayment = getOwnerUsernameToPaymentFromOrder(newOrder);
        addNewTransactionsAfterOrder(newOrder, ownerUsernameToPayment);
        addNewNotificationsAfterOrder(newOrder);
    }

    @Override
    public Collection<DiscountInformationDTO> getAvailableDiscountsFromPendingOrderByUsername(String username) {
        GeneralOrder pendingOrder = usernameToPendingOrder.get(username);
        Collection<DiscountInformationDTO> availableDiscounts = new LinkedList<>();

        for (Store store : pendingOrder.getStores()) {
            List<OrderItem> storeOrderedItems = pendingOrder.getOrderByStore(store).getOrderedItems();
            Collection<DiscountInformationDTO> discountsInStore = store.findAvailableDiscountsByPurchases(storeOrderedItems);
            availableDiscounts.addAll(discountsInStore);
        }

        return Collections.unmodifiableCollection(availableDiscounts);
    }

    @Override
    public void addFeedbackToStoreAfterOrder(String username, int storeId, String feedbackText, Rating rating) {
        GeneralOrder userOrder = usernameToPendingOrder.get(username);
        Store store = storeIdToStore.get(storeId);
        Feedback newFeedback = new Feedback(username, rating, feedbackText, userOrder.getOrderDate(), store.getName(), storeId);
        store.addNewFeedback(newFeedback);

        Notification newNotification = new NewFeedbackNotification(username, store.getName(), rating);
        SDMNotificationsManager.getInstance().addNewNotificationToUser(store.getOwnerUsername(), newNotification);
    }

    @Override
    public void addChosenDiscountOffersToPendingOrderByUsername(String username, List<DiscountOfferDTO> chosenOffers) {
        GeneralOrder pendingOrder = usernameToPendingOrder.get(username);
        Map<Store, List<DiscountOfferDTO>> storeToChosenOffers = getStoreToChosenOffersMapFromChosenOffers(chosenOffers);
        Map<Store, List<OrderItem>> storeToDiscountOfferItems = new HashMap<>();

        for (Store store : storeToChosenOffers.keySet()) {
            List<OrderItem> discountOfferItems = getItemsFromDiscountOffers(storeToChosenOffers.get(store));
            storeToDiscountOfferItems.put(store, discountOfferItems);
        }

        pendingOrder.addItemsFromDiscountOffers(storeToDiscountOfferItems);
    }

    private void addNewNotificationsAfterOrder(GeneralOrder newOrder) {
        Collection<Store> stores = newOrder.getStores();

        for (Store store : stores) {
            SubOrder storeOrder = newOrder.getOrderByStore(store);
            Notification newNotification = new NewOrderNotification(store.getName(), storeOrder.getId(), storeOrder.getCustomerUsername(), storeOrder.getTotalItemsTypes(), storeOrder.getTotalItemsCost(), storeOrder.getDeliveryCost());
            SDMNotificationsManager.getInstance().addNewNotificationToUser(store.getOwnerUsername(), newNotification);
        }
    }

    private Map<Store, List<DiscountOfferDTO>> getStoreToChosenOffersMapFromChosenOffers(List<DiscountOfferDTO> chosenOffers) {
        Map<Store, List<DiscountOfferDTO>> storeToChosenOffers = new HashMap<>();

        for (DiscountOfferDTO chosenOffer : chosenOffers) {
            Store store = storeIdToStore.get(chosenOffer.getStoreId());
            List<DiscountOfferDTO> storeChosenOffers = storeToChosenOffers.getOrDefault(store, new LinkedList<>());
            storeChosenOffers.add(chosenOffer);
            storeToChosenOffers.put(store, storeChosenOffers);
        }

        return storeToChosenOffers;
    }

    private Map<String, Float> getOwnerUsernameToPaymentFromOrder(GeneralOrder newOrder) {
        Set<String> storeOwnersUsernames = newOrder.getStores().stream().map(Store::getOwnerUsername).collect(Collectors.toSet());
        Map<String, Float> ownerUsernameToPayment = storeOwnersUsernames.stream().collect(Collectors.toMap(username -> username, username -> 0.0f));

        for (Store store : newOrder.getStores()) {
            SubOrder storeOrder = newOrder.getOrderByStore(store);
            String ownerUsername = store.getOwnerUsername();
            ownerUsernameToPayment.put(ownerUsername, ownerUsernameToPayment.get(ownerUsername) + storeOrder.getTotalOrderCost());
        }

        return ownerUsernameToPayment;
    }

    private void addNewTransactionsAfterOrder(GeneralOrder newOrder, Map<String, Float> ownerUsernameToPayment) {
        ownerUsernameToPayment.forEach((ownerUsername, payment) -> SDMAccountsManager.getInstance().addNewTransactionToUser(TransactionType.RECEIVE, ownerUsername, payment, newOrder.getOrderDate()));
        SDMAccountsManager.getInstance().addNewTransactionToUser(TransactionType.CHARGE, newOrder.getCustomerUsername(), newOrder.getTotalOrderCost(), newOrder.getOrderDate());
    }

    private void updateAverageOrderItemsCost(GeneralOrder newOrder) {
        averageOrderItemsCost = (averageOrderItemsCost * (orderIdToOrder.size() - 1) + newOrder.getTotalItemsCost()) / orderIdToOrder.size();
    }

    private List<OrderItem> getItemsFromDiscountOffers(List<DiscountOfferDTO> discountOffers) {
        return discountOffers.stream()
                .map(discountOffer -> new OrderItem(storeIdToStore.get(discountOffer.getItem().getSellingStoreId()).getItemById(discountOffer.getItem().getId()), discountOffer.getQuantity(), discountOffer.getItemOfferPrice(), true))
                .collect(Collectors.toList());
    }

    private void updateStoresOrdersAfterNewOrder(GeneralOrder newOrder) {
        newOrder.getStores().forEach(store -> store.addNewOrder(newOrder.getOrderByStore(store)));
    }

    private void updateRegionItemsPurchaseAmountAfterNewOrder(GeneralOrder newOrder) {
        newOrder.getOrderedItems().forEach(orderedItem -> itemIdToItem.get(orderedItem.getItem().getId()).increasePurchaseAmount(orderedItem.getQuantity()));
    }

    private void matchItemsWithStoresToOrderFrom(boolean isDynamicOrder, List<OrderItem> allOrderedItems, Map<Store, List<OrderItem>> storeToOrderedItems, Store chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        if (isDynamicOrder) {
            findCheapestStoresToOrderFrom(allOrderedItems, storeToOrderedItems, itemToItemPurchaseAmount);
        } else {
            storeToOrderedItems.put(chosenStore, new LinkedList<>());
            itemToItemPurchaseAmount.forEach((item, purchaseAmount) -> {
                OrderItem orderedItemToAdd = new OrderItem(chosenStore.getItemById(item.getId()), purchaseAmount, chosenStore.getItemById(item.getId()).getPrice(), false);
                allOrderedItems.add(orderedItemToAdd);
                storeToOrderedItems.get(chosenStore).add(orderedItemToAdd);
            });
        }
    }

    private void findCheapestStoresToOrderFrom(List<OrderItem> allOrderedItems, Map<Store, List<OrderItem>> storeToOrderedItems, Map<RegionItemDTO, Float> itemToItemPurchaseAmount) {
        itemToItemPurchaseAmount.forEach((item, purchaseAmount) -> {
            Store cheapestStore = findCheapestStore(item);
            OrderItem orderedItemToAdd = new OrderItem(cheapestStore.getItemById(item.getId()), purchaseAmount, cheapestStore.getItemById(item.getId()).getPrice(), false);
            allOrderedItems.add(orderedItemToAdd);
            if (!storeToOrderedItems.containsKey(cheapestStore)) {
                storeToOrderedItems.put(cheapestStore, new LinkedList<>());
            }

            storeToOrderedItems.get(cheapestStore).add(orderedItemToAdd);
        });
    }

    private Store findCheapestStore(RegionItemDTO item) {
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
        return cheapestStore;
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
                .anyMatch(currentLocation -> currentLocation.equals(location));
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
