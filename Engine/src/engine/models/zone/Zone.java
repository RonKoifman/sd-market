package engine.models.zone;

import engine.models.order.GeneralOrder;
import engine.models.store.Store;
import engine.models.user.StoreOwner;

import java.util.HashMap;
import java.util.Map;

public class Zone {

    private final StoreOwner owner;
    private final String name;
    private final Map<Integer, Store> storeIdToStore;
    private final Map<Integer, GeneralOrder> orderIdToOrder = new HashMap<>();
    private int totalItemsTypes;
    private int averageOrderedItemsCost;

    public Zone(StoreOwner owner, String name, Map<Integer, Store> storeIdToStore) {
        this.owner = owner;
        this.name = name;
        this.storeIdToStore = storeIdToStore;
    }
}
