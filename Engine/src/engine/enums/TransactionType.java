package engine.enums;

public enum TransactionType {
    DEPOSIT("Deposit"), RECEIVE("Receive"), CHARGE("Charge");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
