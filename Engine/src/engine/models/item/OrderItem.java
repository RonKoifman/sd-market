package engine.models.item;

import dto.models.OrderItemDTO;

public class OrderItem {

    private final StoreItem item;
    private final float quantity;
    private final float itemOrderPrice;
    private final boolean isFromDiscount;

    public OrderItem(StoreItem item, float quantity, float itemOrderPrice, boolean isFromDiscount) {
        this.item = item;
        this.quantity = quantity;
        this.itemOrderPrice = itemOrderPrice;
        this.isFromDiscount = isFromDiscount;
    }

    public OrderItemDTO toOrderItemDTO() {
        return new OrderItemDTO.Builder()
                .item(item.toStoreItemDTO())
                .isFromDiscount(isFromDiscount)
                .quantity(quantity)
                .itemOrderPrice(itemOrderPrice)
                .build();
    }

    public StoreItem getItem() {
        return item;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getItemOrderPrice() {
        return itemOrderPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", itemOrderPrice=" + itemOrderPrice +
                ", isFromDiscount=" + isFromDiscount +
                '}';
    }
}
