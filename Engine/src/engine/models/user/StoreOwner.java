package engine.models.user;

import dto.models.FeedbackDTO;
import dto.models.StoreDTO;
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

    public Map<StoreDTO, List<SubOrderDTO>> getStoreToOrdersByRegionName(String regionName) {
        Map<StoreDTO, List<SubOrderDTO>> storeToOrders = new HashMap<>();
        Collection<Store> ownedStores = regionNameToOwnedStoresInRegion.getOrDefault(regionName, new HashSet<>());

        ownedStores.forEach(store -> storeToOrders.put(store.toStoreDTO(), store.getOrdersMade().stream().map(SubOrder::toSubOrderDTO).collect(Collectors.toList())));

        return Collections.unmodifiableMap(storeToOrders);
    }

    public Collection<FeedbackDTO> getOwnedStoresFeedbacksByRegionName(String regionName) {
        return Collections.unmodifiableCollection(regionNameToOwnedStoresInRegion.getOrDefault(regionName, new HashSet<>())
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
