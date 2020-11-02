package engine.managers;

import dto.models.*;
import engine.enums.UserRole;
import engine.models.order.GeneralOrder;
import engine.models.store.Store;

import java.util.*;

public interface UsersManager {

    void addNewUser(String username, UserRole userRole);

    Collection<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);

    void addNewOrderToCustomer(String customerUsername, GeneralOrder newOrder, String regionName);

    void addNewStoreToStoreOwner(String ownerUsername, Store newStore, String regionName);

    Collection<GeneralOrderDTO> getCustomerOrdersByRegionName(String customerUsername, String regionName);

    Collection<StoreDTO> getStoreOwnerOwnedStoresByRegionName(String storeOwnerUsername, String regionName);
}
