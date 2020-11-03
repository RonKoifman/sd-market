package engine.managers;

import dto.models.*;
import engine.enums.Rating;
import engine.interfaces.Identifiable;
import engine.interfaces.Transferable;

import java.awt.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SingleRegionManager extends Transferable<RegionDTO> {

    void deleteItemFromStore(int storeId, int itemId);

    void addNewItemToStore(int storeId, int itemId, float itemPrice);

    void updateItemPriceInStore(int storeId, int itemId, float newItemPrice);

    void addNewStoreToRegion(int storeId, String ownerUsername, String storeName, Point storeLocation, float storeDeliveryPPK, Map<Integer, Float> itemIdToItemPriceInStore);

    void addNewItemToRegion(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Float> storeIdToItemPriceInStore);

    void checkForFreeLocation(Point location);

    void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, Point orderDestination, String customerUsername, StoreDTO chosenStore, Map<RegionItemDTO, Float> itemToItemPurchaseAmount);

    GeneralOrderDTO getPendingOrderByUsername(String username);

    Collection<DiscountInformationDTO> getAvailableDiscountsFromPendingOrderByUsername(String username);

    void addChosenDiscountOffersToPendingOrderByUsername(String username, List<DiscountOfferDTO> chosenDiscountOffers);

    void confirmPendingOrderByUsername(String username);

    void addFeedbackToStoreAfterOrder(String username, int storeId, String feedbackText, Rating rating);

    StoreDTO getStoreById(int storeId);

    RegionItemDTO getItemById(int itemId);

    Collection<RegionItemDTO> getAllItemsInRegion();

    Collection<StoreDTO> getAllStoresInRegion();

    Collection<GeneralOrderDTO> getAllOrdersInRegion();

    String getRegionName();

    String getRegionOwnerUsername();

    RegionDTO toDTO();
}
