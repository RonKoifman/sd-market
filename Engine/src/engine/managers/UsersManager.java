package engine.managers;

import dto.models.*;
import engine.enums.UserRole;
import engine.models.order.GeneralOrder;
import engine.models.store.Store;

import java.util.*;

public interface UsersManager {

    void addNewUser(String username, UserRole userRole);

    Set<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);

    void addNewOrderToCustomer(String customerUsername, GeneralOrder newOrder, String regionName);

    void addNewStoreToStoreOwner(String ownerUsername, Store newStore, String regionName);

    Collection<GeneralOrderDTO> getCustomerOrdersByRegionName(String customerUsername, String regionName);

    Map<StoreDTO, List<SubOrderDTO>> getStoreOwnerStoreToOrdersByRegionName(String storeOwnerUsername, String regionName);

    Collection<FeedbackDTO> getStoreOwnerOwnedStoresFeedbacksByRegionName(String storeOwnerUsername, String regionName);
}
