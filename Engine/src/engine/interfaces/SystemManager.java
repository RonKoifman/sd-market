package engine.interfaces;

import dto.models.*;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SystemManager {

    boolean isFileLoaded();

    void loadDataFromFile(String filePath) throws JAXBException;

    void deleteItemFromStore(int storeId, int itemId);

    void addNewItemToStore(int storeId, int itemId, int itemPrice);

    void updateItemPriceInStore(int storeId, int itemId, int newItemPrice);

    void addNewStoreToMarket(int storeId, String storeName, Point storeLocation, int storeDeliveryPPK, Map<Integer, Integer> itemIdToItemPriceInStore);

    void addNewItemToMarket(int itemId, String itemName, String itemPurchaseForm, Map<Integer, Integer> storeIdToItemPriceInStore);

    void addPendingOrderToOrdersStock();

    GeneralOrderDTO getPendingOrder();

    void createNewPendingOrder(boolean isDynamicOrder, LocalDate orderDate, CustomerDTO customer, StoreDTO chosenStore, Map<MarketItemDTO, Float> itemToItemPurchaseAmount);

    Map<StoreDTO, Collection<DiscountInformationDTO>> getAvailableDiscountsFromPendingOrder();

    void addChosenDiscountOffersToPendingOrder(Map<StoreDTO, List<DiscountOfferDTO>> storeToDiscountOffers);

    StoreDTO getStoreById(int storeId);

    MarketItemDTO getItemById(int itemId);

    Collection<MarketItemDTO> getAllItemsInMarket();

    Collection<StoreDTO> getAllStoresInMarket();

    Collection<GeneralOrderDTO> getAllOrdersInMarket();

    Collection<UserDTO> getAllUsersInMarket();
}
