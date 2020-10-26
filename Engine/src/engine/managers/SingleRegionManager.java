package engine.managers;

import dto.models.*;
import engine.enums.Rating;

import java.awt.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SingleRegionManager {

    void deleteItemFromStore(int storeId, int itemId);

    void addNewItemToStore(int storeId, int itemId, float itemPrice);

    void updateItemPriceInStore(int storeId, int itemId, float newItemPrice);

    void addNewStoreToRegion(String ownerUsername, int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore);

    void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore);

    void checkForValidOrderDestination(Point orderDestination);

    void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, String customerUsername, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount);

    GeneralOrderDTO getPendingOrderByUsername(String username);

    Collection<DiscountInformationDTO> getAvailableDiscountsFromPendingOrderByUsername(String username);

    void addChosenDiscountOffersToPendingOrderByUsername(String username, List<DiscountOfferDTO> chosenDiscountOffers);

    void confirmPendingOrderByUsername(String username);

    void addFeedbackToStoreAfterOrder(String username, int storeId, String userMessage, Rating rating);

    StoreDTO getStoreById(int storeId);

    RegionItemDTO getItemById(int itemId);

    Collection<RegionItemDTO> getAllItemsInRegion();

    Collection<StoreDTO> getAllStoresInRegion();

    Collection<GeneralOrderDTO> getAllOrdersInRegion();

    RegionDTO getRegionDetails();
}
