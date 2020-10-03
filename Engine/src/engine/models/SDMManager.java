package engine.models;

import dto.models.*;
import engine.enums.PurchaseForm;
import engine.exceptions.*;
import engine.interfaces.Locationable;
import engine.interfaces.SystemManager;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SDMManager implements SystemManager {

    private static SDMManager instance;
    private final FileHandler fileHandler = new FileHandler();
    private Map<Integer, Store> storeIdToStore;
    private Map<Integer, MarketItem> itemIdToItem;
    private Map<Integer, Customer> customerIdToCustomer;
    private Map<Location, Locationable> locationToLocationable;
    private final Map<Integer, GeneralOrder> orderIdToOrder = new HashMap<>();
    private GeneralOrder pendingOrder;

    private SDMManager() {
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            synchronized (SDMManager.class) {
                if (instance == null) {
                    instance = new SDMManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void loadDataFromFile(String filePath) throws JAXBException {
        fileHandler.loadSystemDataFromFile(filePath);
        initializeMarket();
    }

    @Override
    public boolean isFileLoaded() {
        return fileHandler.isFileLoaded();
    }

    @Override
    public StoreDTO getStoreById(int storeId) {
        if (!storeIdToStore.containsKey(storeId)) {
            throw new IdNotExistsException();
        }

        return storeIdToStore.get(storeId).toStoreDTO();
    }

    @Override
    public MarketItemDTO getItemById(int itemId) {
        if (!itemIdToItem.containsKey(itemId)) {
            throw new IdNotExistsException();
        }

        return itemIdToItem.get(itemId).toMarketItemDTO();
    }

    @Override
    public Collection<MarketItemDTO> getAllItemsInMarket() {
        return itemIdToItem.values()
                .stream()
                .map(MarketItem::toMarketItemDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<StoreDTO> getAllStoresInMarket() {
        return storeIdToStore.values()
                .stream()
                .map(Store::toStoreDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<GeneralOrderDTO> getAllOrdersInMarket() {
       return orderIdToOrder.values()
               .stream()
               .map(GeneralOrder::toGeneralOrderDTO)
               .collect(Collectors.toSet());
    }

    @Override
    public Collection<CustomerDTO> getAllCustomersInMarket() {
        return customerIdToCustomer.values()
                .stream()
                .map(Customer::toCustomerDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteItemFromStore(int storeId, int itemId) {
        Store store = storeIdToStore.get(storeId);
        MarketItem itemToDelete = itemIdToItem.get(itemId);

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
    public void addNewItemToStore(int storeId, int itemId, int itemPrice) {
        Store store = storeIdToStore.get(storeId);
        MarketItem itemToAdd = itemIdToItem.get(itemId);

        if (store.isItemSold(itemId)) {
            throw new ItemAlreadySoldByStoreException();
        }

        store.addNewItem(new StoreItem(itemToAdd.getId(), itemToAdd.getName(), itemToAdd.getPurchaseForm(), store, itemPrice));
        itemToAdd.increaseAmountOfStoresSelling(itemPrice);
    }

    @Override
    public void updateItemPriceInStore(int storeId, int itemId, int newItemPrice) {
        Store store = storeIdToStore.get(storeId);
        MarketItem itemToUpdate = itemIdToItem.get(itemId);

        if (!store.isItemSold(itemId)) {
            throw new ItemNotSoldByStoreException();
        }

        store.updateItemPrice(itemId, newItemPrice);
        itemToUpdate.updateAveragePrice(store.getItemById(itemId).getPrice(), newItemPrice);
    }

    @Override
    public void addNewItemToMarket(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore) {
        if (itemIdToItem.containsKey(itemId)) {
            throw new IllegalStateException("The item id '" + itemId + "' is already taken.");
        }

        MarketItem newItem = new MarketItem(itemId, itemName, PurchaseForm.valueOf(itemPurchaseForm.trim().toUpperCase()));
        itemIdToItem.put(newItem.getId(), newItem);
        storeIdToItemPriceInStore.forEach((storeId, itemPriceInStore) -> addNewItemToStore(storeId, itemId, itemPriceInStore));
    }

    @Override
    public void addNewStoreToMarket(int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore) {
        if (storeIdToStore.containsKey(storeId)) {
            throw new IllegalStateException("The store id '" + storeId + "' is already taken.");
        }

        if (locationToLocationable.containsKey(new Location(storeLocation.x, storeLocation.y))) {
            throw new IdenticalLocationsException(String.format("The location (%d, %d) is already taken.", storeLocation.x, storeLocation.y));
        }

        Store newStore = new Store(storeId, storeName, storeDeliveryPPK, new Location(storeLocation.x, storeLocation.y));
        itemIdToItemPriceInStore.forEach((itemId, itemPrice) -> addNewItemToStore(storeId, itemId, itemPrice));
        storeIdToStore.put(newStore.getId(), newStore);
        locationToLocationable.put(newStore.getLocation(), newStore);
    }

    @Override
    public GeneralOrderDTO getPendingOrder() {
        return pendingOrder.toGeneralOrderDTO();
    }

    @Override
    public void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, CustomerDTO customer, StoreDTO chosenStore, Map<MarketItemDTO, Float> itemToItemPurchaseAmount) {
        List<OrderItemDTO> allOrderedItems = new LinkedList<>();
        Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems = new HashMap<>();
        matchItemsWithStoresToOrderFrom(isDynamicOrder, allOrderedItems, storeToOrderedItems, chosenStore, itemToItemPurchaseAmount);
        pendingOrder = new GeneralOrder(customer, orderDate, allOrderedItems, storeToOrderedItems);
    }

    @Override
    public void addPendingOrderToOrdersStock() {
        pendingOrder.generateOrderId();
        orderIdToOrder.put(pendingOrder.getId(), pendingOrder);
        customerIdToCustomer.get(pendingOrder.getCustomer().getId()).addNewOrder(pendingOrder);
        updateItemsPurchaseAmountAfterNewOrder(pendingOrder);
        updateStoresOrdersAfterNewOrder(pendingOrder);
    }

    @Override
    public Map<StoreDTO, Collection<DiscountInformationDTO>> getAvailableDiscountsFromPendingOrder() {
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
    public void addChosenDiscountOffersToPendingOrder(Map<StoreDTO, List<DiscountOfferDTO>> storeToDiscountOffers) {
        Map<StoreDTO, List<OrderItemDTO>> storeToDiscountOfferItems = new HashMap<>();

        for (StoreDTO store : storeToDiscountOffers.keySet()) {
            List<OrderItemDTO> discountOfferItems = getItemsFromDiscountOffers(storeToDiscountOffers.get(store));
            storeToDiscountOfferItems.put(store, discountOfferItems);
        }

        pendingOrder.addItemsFromDiscountOffers(storeToDiscountOfferItems);
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

    private void matchItemsWithStoresToOrderFrom(boolean isDynamicOrder, List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems, StoreDTO chosenStore, Map<MarketItemDTO, Float> itemToItemPurchaseAmount) {
        if (isDynamicOrder) {
            findCheapestStoresToOrderFrom(allOrderedItems, storeToOrderedItems, itemToItemPurchaseAmount);
        } else {
            storeToOrderedItems.put(chosenStore, new LinkedList<>());
            for (Map.Entry<MarketItemDTO, Float> entry : itemToItemPurchaseAmount.entrySet()) {
                OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(chosenStore.getItemById(entry.getKey().getId())).itemOrderPrice(chosenStore.getItemById(entry.getKey().getId()).getPrice()).quantity(entry.getValue()).isFromDiscount(false).build();
                allOrderedItems.add(orderedItemToAdd);
                storeToOrderedItems.get(chosenStore).add(orderedItemToAdd);
            }
        }
    }

    private void findCheapestStoresToOrderFrom(List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems, Map<MarketItemDTO, Float> itemToItemPurchaseAmount) {
        for (Map.Entry<MarketItemDTO, Float> entry : itemToItemPurchaseAmount.entrySet()) {
            StoreDTO cheapestStore = findCheapestStore(entry.getKey());
            OrderItemDTO orderedItemToAdd = new OrderItemDTO.Builder().item(cheapestStore.getItemById(entry.getKey().getId())).itemOrderPrice(cheapestStore.getItemById(entry.getKey().getId()).getPrice()).quantity(entry.getValue()).isFromDiscount(false).build();
            allOrderedItems.add(orderedItemToAdd);
            if (!storeToOrderedItems.containsKey(cheapestStore)) {
                storeToOrderedItems.put(cheapestStore, new LinkedList<>());
            }

            storeToOrderedItems.get(cheapestStore).add(orderedItemToAdd);
        }
    }

    private StoreDTO findCheapestStore(MarketItemDTO item) {
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

    private void initializeMarket() {
        itemIdToItem = fileHandler.getTempItemIdToItem();
        storeIdToStore = fileHandler.getTempStoreIdToStore();
        locationToLocationable = fileHandler.getTempLocationToLocationable();
        customerIdToCustomer = new HashMap<>();
        orderIdToOrder.clear();
        Order.resetOrdersCount();
        initializeItemsSellsInStores();
    }

    private void initializeItemsSellsInStores() {
        for (MarketItem item : itemIdToItem.values()) {
            for (Store store : storeIdToStore.values()) {
                if (store.isItemSold(item.getId())) {
                    item.increaseAmountOfStoresSelling(store.getItemById(item.getId()).getPrice());
                }
            }
        }
    }

    @Override
    public String toString() {
        return "SDMManager{" +
                "fileHandler=" + fileHandler +
                ", storeIdToStore=" + storeIdToStore +
                ", itemIdToItem=" + itemIdToItem +
                ", customerIdToCustomer=" + customerIdToCustomer +
                ", locationToLocationable=" + locationToLocationable +
                ", orderIdToOrder=" + orderIdToOrder +
                ", pendingOrder=" + pendingOrder +
                '}';
    }
}
