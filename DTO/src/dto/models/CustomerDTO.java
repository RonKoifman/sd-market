package dto.models;

import java.awt.*;
import java.util.Objects;

public class CustomerDTO {

    private final int id;
    private final String name;
    private final Point location;
    private final int amountOfOrdersMade;
    private final float averageItemsPayment;
    private final float averageDeliveriesPayment;

    private CustomerDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.location = builder.location;
        this.amountOfOrdersMade = builder.amountOfOrdersMade;
        this.averageItemsPayment = builder.averageItemsPayment;
        this.averageDeliveriesPayment = builder.averageDeliveriesPayment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

    public int getAmountOfOrdersMade() {
        return amountOfOrdersMade;
    }

    public float getAverageItemsPayment() {
        return averageItemsPayment;
    }

    public float getAverageDeliveriesPayment() {
        return averageDeliveriesPayment;
    }

    public static final class Builder {

        private int id;
        private String name;
        private Point location;
        private int amountOfOrdersMade;
        private float averageItemsPayment;
        private float averageDeliveriesPayment;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder location(Point location) {
            this.location = location;
            return this;
        }

        public Builder amountOfOrdersMade(int amountOfOrdersMade) {
            this.amountOfOrdersMade = amountOfOrdersMade;
            return this;
        }

        public Builder averageItemsPayment(float averageItemsPayment) {
            this.averageItemsPayment = averageItemsPayment;
            return this;
        }

        public Builder averageDeliveriesPayment(float averageDeliveriesPayment) {
            this.averageDeliveriesPayment = averageDeliveriesPayment;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", amountOfOrdersMade=" + amountOfOrdersMade +
                ", averageItemsPayment=" + averageItemsPayment +
                ", averageDeliveriesPayment=" + averageDeliveriesPayment +
                '}';
    }
}
