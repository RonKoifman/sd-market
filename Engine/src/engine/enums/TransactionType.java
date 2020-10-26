package engine.enums;

public enum TransactionType {
    DEPOSIT("Deposit"), RECEIVE("Receive"), PAYMENT("Payment");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
