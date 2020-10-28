package engine.models.user;

import dto.models.FeedbackDTO;
import dto.models.SubOrderDTO;
import engine.enums.UserRole;
import engine.models.feedback.Feedback;
import engine.models.order.SubOrder;
import engine.models.store.Store;

import java.util.*;
import java.util.stream.Collectors;

public class StoreOwner extends User {

    private final Map<String, Set<Store>> regionNameToOwnedStoresInRegion = new HashMap<>();

    public StoreOwner(String username) {
        super(username, UserRole.STORE_OWNER);
    }

    public void addNewOwnedStore(String regionName, Store newStore) {
        if (!regionNameToOwnedStoresInRegion.containsKey(regionName)) {
            regionNameToOwnedStoresInRegion.put(regionName, new HashSet<>());
        }

        regionNameToOwnedStoresInRegion.get(regionName).add(newStore);
        newStore.setOwnerUsername(username);
    }

    public Collection<SubOrderDTO> getOwnedStoreOrdersByRegionName(int storeId, String regionName) {
        Collection<SubOrderDTO> storeOrders;
        Collection<Store> ownedStores = regionNameToOwnedStoresInRegion.getOrDefault(regionName, Collections.emptySet());

        if (ownedStores.isEmpty()) {
            storeOrders = Collections.emptySet();
        } else {
            Store chosenStore = ownedStores.stream().filter(store -> store.getId() == storeId).findFirst().get();
            storeOrders = chosenStore.getOrdersMade().stream().map(SubOrder::toSubOrderDTO).collect(Collectors.toSet());
        }

        return Collections.unmodifiableCollection(storeOrders);
    }

    public Collection<FeedbackDTO> getOwnedStoresFeedbacksByRegionName(String regionName) {
        return Collections.unmodifiableCollection(regionNameToOwnedStoresInRegion.getOrDefault(regionName, Collections.emptySet())
                .stream()
                .map(Store::getFeedbacksReceived)
                .flatMap(Collection::stream)
                .map(Feedback::toFeedbackDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "StoreOwner{" +
                "regionNameToOwnedStoresInRegion=" + regionNameToOwnedStoresInRegion +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
