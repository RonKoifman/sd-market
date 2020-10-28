package engine.managers;

import dto.models.*;
import engine.enums.UserRole;
import engine.models.feedback.Feedback;
import engine.models.order.GeneralOrder;
import engine.models.store.Store;
import engine.models.user.Customer;
import engine.models.user.StoreOwner;
import engine.models.user.User;

import java.util.*;
import java.util.stream.Collectors;

public class SDMUsersManager implements UsersManager {

    private static SDMUsersManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final Map<String, User> usernameToUser = new HashMap<>();

    private SDMUsersManager() {
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMUsersManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void addNewUser(String username, UserRole userRole) {
        if (isUserExists(username)) {
            throw new IllegalStateException("The user '" + username + "' already exists.");
        }

        switch (userRole) {
            case STORE_OWNER:
                usernameToUser.put(username, new StoreOwner(username));
                break;

            case CUSTOMER:
                usernameToUser.put(username, new Customer(username));
                break;
        }

        SDMAccountsManager.getInstance().createNewAccountToUser(username);
    }

    @Override
    public Collection<UserDTO> getUsers() {
        return Collections.unmodifiableSet(usernameToUser.values()
                .stream()
                .map(User::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean isUserExists(String username) {
        return usernameToUser.keySet()
                .stream()
                .anyMatch(currentUsername -> currentUsername.toLowerCase().equals(username.toLowerCase()));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
       return usernameToUser.get(username).toDTO();
    }

    @Override
    public void addNewOrderToCustomer(String customerUsername, GeneralOrder newOrder, String regionName) {
        Customer customer = (Customer)usernameToUser.get(customerUsername);
        customer.addNewOrder(regionName, newOrder);
    }

    @Override
    public void addNewStoreToStoreOwner(String ownerUsername, Store newStore, String regionName) {
        StoreOwner storeOwner = (StoreOwner)usernameToUser.get(ownerUsername);
        storeOwner.addNewOwnedStore(regionName, newStore);
    }

    @Override
    public Collection<GeneralOrderDTO> getCustomerOrdersByRegionName(String customerUsername, String regionName) {
        Customer customer = (Customer)usernameToUser.get(customerUsername);
        Collection<GeneralOrder> customerOrders = customer.getOrdersByRegionName(regionName);

        return Collections.unmodifiableCollection(customerOrders.stream()
                .map(GeneralOrder::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<StoreDTO> getStoreOwnerOwnedStoresByRegionName(String storeOwnerUsername, String regionName) {
        StoreOwner storeOwner = (StoreOwner)usernameToUser.get(storeOwnerUsername);
        Collection<Store> ownedStores = storeOwner.getOwnedStoresByRegionName(regionName);

        return Collections.unmodifiableCollection(ownedStores.stream()
                .map(Store::toDTO)
                .collect(Collectors.toSet()));
    }

    @Override
    public Collection<FeedbackDTO> getStoreOwnerOwnedStoresFeedbacksByRegionName(String storeOwnerUsername, String regionName) {
        StoreOwner storeOwner = (StoreOwner)usernameToUser.get(storeOwnerUsername);
        Collection<Feedback> feedbacks = storeOwner.getOwnedStoresFeedbacksByRegionName(regionName);

        return Collections.unmodifiableCollection(feedbacks.stream()
                .map(Feedback::toDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "SDMUsersManager{" +
                "usernameToUser=" + usernameToUser +
                '}';
    }
}
