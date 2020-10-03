package engine.enums;

public enum PurchaseForm {
    WEIGHT("Weight"), QUANTITY("Quantity");

    private final String value;

    PurchaseForm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
