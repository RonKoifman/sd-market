package engine.models.user;

import dto.models.CustomerDTO;
import engine.interfaces.Locationable;
import engine.interfaces.Identifiable;
import engine.models.location.Location;
import engine.models.order.GeneralOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Customer implements Locationable, Identifiable {

    private final int id;
    private final String name;
    private final Location location;
    private final Map<Integer, GeneralOrder> orderIdToOrder = new HashMap<>();
    private int amountOfOrdersMade;
    private float averageItemsPayment;
    private float averageDeliveriesPayment;

    public Customer(int id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return id;
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO.Builder()
                .id(id)
                .name(name)
                .location(location)
                .amountOfOrdersMade(amountOfOrdersMade)
                .averageItemsPayment(averageItemsPayment)
                .averageDeliveriesPayment(averageDeliveriesPayment)
                .build();
    }

    public void addNewOrder(GeneralOrder newOrder) {
        orderIdToOrder.put(newOrder.getId(), newOrder);
        amountOfOrdersMade++;
        averageItemsPayment = (averageItemsPayment * (amountOfOrdersMade - 1) + newOrder.getTotalItemsCost()) / amountOfOrdersMade;
        averageDeliveriesPayment = (averageDeliveriesPayment * (amountOfOrdersMade - 1) + newOrder.getDeliveryCost()) / amountOfOrdersMade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", amountOfOrdersMade=" + amountOfOrdersMade +
                ", averageItemsPayment=" + averageItemsPayment +
                ", averageDeliveriesPayment=" + averageDeliveriesPayment +
                '}';
    }
}
