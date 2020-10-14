package engine.managers;

import dto.models.*;

import java.awt.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SingleRegionManager {

    void deleteItemFromStore(int storeId, int itemId);

    void addNewItemToStore(int storeId, int itemId, int itemPrice);

    void updateItemPriceInStore(int storeId, int itemId, int newItemPrice);

    void addNewStoreToRegion(String ownerUsername, int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore);

    void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore);

    void addPendingOrderToOrdersStock();

    GeneralOrderDTO getPendingOrder();

    void checkForValidOrderDestination(Point orderDestination);

    void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, CustomerDTO customer, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount);

    Map<StoreDTO, Collection<DiscountInformationDTO>> getAvailableDiscountsFromPendingOrder();

    void addChosenDiscountOffersToPendingOrder(Map<StoreDTO, List<DiscountOfferDTO>> storeToDiscountOffers);

    void addFeedbacksToPendingOrder(Map<StoreDTO, List<FeedbackDTO>> storeToFeedbacks);

    StoreDTO getStoreById(int storeId);

    RegionItemDTO getItemById(int itemId);

    Collection<RegionItemDTO> getAllItemsInRegion();

    Collection<StoreDTO> getAllStoresInRegion();

    Collection<GeneralOrderDTO> getAllOrdersInRegion();

    RegionDTO getRegionDTO();
}
