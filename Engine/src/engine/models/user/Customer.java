package engine.models.user;

import engine.enums.UserRole;
import engine.models.order.GeneralOrder;

import java.util.*;

public class Customer extends User {

    private final Map<String, Set<GeneralOrder>> regionNameToOrdersFromRegion = new HashMap<>();

    public Customer(String username) {
        super(username, UserRole.CUSTOMER);
    }

    public void addNewOrder(String regionName, GeneralOrder newOrder) {
        if (!regionNameToOrdersFromRegion.containsKey(regionName)) {
            regionNameToOrdersFromRegion.put(regionName, new HashSet<>());
        }

        regionNameToOrdersFromRegion.get(regionName).add(newOrder);
    }

    public Collection<GeneralOrder> getOrdersByRegionName(String regionName) {
        Collection<GeneralOrder> orders = regionNameToOrdersFromRegion.getOrDefault(regionName, new HashSet<>());

        return Collections.unmodifiableCollection(orders);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "regionNameToOrdersFromRegion=" + regionNameToOrdersFromRegion +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
