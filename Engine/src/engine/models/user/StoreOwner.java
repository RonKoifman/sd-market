package engine.models.user;

import engine.enums.UserRole;
import engine.models.feedback.Feedback;
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

    public Collection<Store> getOwnedStoresByRegionName(String regionName) {
       return Collections.unmodifiableCollection(regionNameToOwnedStoresInRegion.getOrDefault(regionName, Collections.emptySet()));
    }

    public Collection<Feedback> getOwnedStoresFeedbacksByRegionName(String regionName) {
        return Collections.unmodifiableCollection(regionNameToOwnedStoresInRegion.getOrDefault(regionName, Collections.emptySet())
                .stream()
                .map(Store::getFeedbacksReceived)
                .flatMap(Collection::stream)
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
