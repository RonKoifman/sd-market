package engine.models.order;

import dto.models.*;
import engine.interfaces.Transferable;
import engine.models.item.OrderItem;
import engine.models.location.Location;
import engine.models.store.Store;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralOrder extends Order implements Transferable<GeneralOrderDTO> {

    private final Map<Store, SubOrder> storeToOrder = new HashMap<>();

    public GeneralOrder(String regionName, String customerUsername, Location orderDestination, LocalDate orderDate, List<OrderItem> allOrderedItems, Map<Store, List<OrderItem>> storeToOrderedItems) {
        super(regionName, customerUsername, orderDate, orderDestination, allOrderedItems);
        createStoresSubOrders(storeToOrderedItems);
        this.totalItemsCost = calculateTotalItemsCost();
        this.deliveryCost = calculateDeliveryCost();
        this.totalOrderCost = calculateTotalOrderCost();
        this.totalItemsTypes = calculateTotalItemsTypes();
        this.totalItemsAmount = calculateTotalItemsAmount();
    }

    @Override
    public GeneralOrderDTO toDTO() {
        return new GeneralOrderDTO.Builder()
                .id(id)
                .orderDate(String.format("%d/%d/%d", orderDate.getDayOfMonth(), orderDate.getMonthValue(), orderDate.getYear()))
                .customerUsername(customerUsername)
                .orderDestination(orderDestination)
                .orderedItems(orderedItems.stream().map(OrderItem::toDTO).collect(Collectors.toList()))
                .subOrders(storeToOrder.values().stream().map(SubOrder::toDTO).collect(Collectors.toList()))
                .stores(storeToOrder.keySet().stream().map(Store::toDTO).collect(Collectors.toList()))
                .totalItemsCost(totalItemsCost)
                .deliveryCost(deliveryCost)
                .totalOrderCost(totalOrderCost)
                .totalItemsTypes(totalItemsTypes)
                .totalItemsAmount(totalItemsAmount)
                .build();
    }

    public Collection<Store> getStores() {
        return Collections.unmodifiableCollection(storeToOrder.keySet());
    }

    public SubOrder getOrderByStore(Store store) {
        return storeToOrder.get(store);
    }

    public void addItemsFromDiscountOffers(Map<Store, List<OrderItem>> storeToDiscountOfferItems) {
        addDiscountItemsToStoresSubOrders(storeToDiscountOfferItems);
        totalItemsCost = calculateTotalItemsCost();
        totalOrderCost = calculateTotalOrderCost();
        totalItemsTypes = calculateTotalItemsTypes();
        totalItemsAmount = calculateTotalItemsAmount();
    }

    @Override
    public void generateOrderId() {
        super.generateOrderId();
        storeToOrder.values().forEach(subOrder -> subOrder.setOrderId(id));
    }

    @Override
    protected float calculateDeliveryCost() {
        return storeToOrder.values()
                .stream()
                .map(SubOrder::getDeliveryCost)
                .reduce(0.0f, Float::sum);
    }

    private void addDiscountItemsToStoresSubOrders(Map<Store, List<OrderItem>> storeToDiscountOfferItems) {
        for (Store store : storeToDiscountOfferItems.keySet()) {
            getOrderByStore(store).addItemsFromDiscountOffers(storeToDiscountOfferItems.get(store));
            orderedItems.addAll(storeToDiscountOfferItems.get(store));
        }
    }

    private void createStoresSubOrders(Map<Store, List<OrderItem>> storeToOrderedItems) {
        for (Store store : storeToOrderedItems.keySet()) {
            storeToOrder.put(store, new SubOrder(regionName, store, customerUsername, orderDestination, orderDate, storeToOrderedItems.get(store)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralOrder that = (GeneralOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GeneralOrder{" +
                "storeToOrder=" + storeToOrder +
                ", id=" + id +
                ", regionName='" + regionName + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", orderDestination=" + orderDestination +
                ", orderDate=" + orderDate +
                ", orderedItems=" + orderedItems +
                ", totalItemsCost=" + totalItemsCost +
                ", deliveryCost=" + deliveryCost +
                ", totalOrderCost=" + totalOrderCost +
                ", totalItemsTypes=" + totalItemsTypes +
                ", totalItemsAmount=" + totalItemsAmount +
                '}';
    }
}
