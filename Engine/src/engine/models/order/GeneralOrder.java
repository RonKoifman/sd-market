package engine.models.order;

import dto.models.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralOrder extends Order {

    private final Map<StoreDTO, SubOrder> storeToOrder = new HashMap<>();

    public GeneralOrder(CustomerDTO customer, LocalDate orderDate, List<OrderItemDTO> allOrderedItems, Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems) {
        super(customer, orderDate, allOrderedItems);
        createStoresSubOrders(storeToOrderedItems);
        this.totalItemsCost = calculateTotalItemsCost();
        this.deliveryCost = calculateDeliveryCost();
        this.totalOrderCost = calculateTotalOrderCost();
        this.totalItemsTypes = calculateTotalItemsTypes();
        this.totalItemsAmount = calculateTotalItemsAmount();
    }

    public GeneralOrderDTO toGeneralOrderDTO() {
        return new GeneralOrderDTO.Builder()
                .id(id)
                .orderDate(orderDate)
                .orderedItems(orderedItems)
                .storeToOrder(storeToOrder.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toSubOrderDTO())))
                .totalItemsCost(totalItemsCost)
                .deliveryCost(deliveryCost)
                .totalOrderCost(totalOrderCost)
                .totalItemsTypes(totalItemsTypes)
                .totalItemsAmount(totalItemsAmount)
                .build();
    }

    public Collection<StoreDTO> getStores() {
        return storeToOrder.keySet();
    }

    public SubOrder getOrderByStore(StoreDTO store) {
        return storeToOrder.get(store);
    }

    public void addItemsFromDiscountOffers(Map<StoreDTO, List<OrderItemDTO>> storeToDiscountOfferItems) {
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

    private void addDiscountItemsToStoresSubOrders(Map<StoreDTO, List<OrderItemDTO>> storeToDiscountOfferItems) {
        for (StoreDTO store : storeToDiscountOfferItems.keySet()) {
            getOrderByStore(store).addItemsFromDiscountOffers(storeToDiscountOfferItems.get(store));
            orderedItems.addAll(storeToDiscountOfferItems.get(store));
        }
    }

    private void createStoresSubOrders(Map<StoreDTO, List<OrderItemDTO>> storeToOrderedItems) {
        for (StoreDTO store : storeToOrderedItems.keySet()) {
            storeToOrder.put(store, new SubOrder(store, customer, orderDate, storeToOrderedItems.get(store)));
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
                ", customer=" + customer +
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
