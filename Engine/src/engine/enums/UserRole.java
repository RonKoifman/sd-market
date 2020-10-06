package engine.enums;

public enum UserRole {
    CUSTOMER("Customer"), STORE_OWNER("Store Owner");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
