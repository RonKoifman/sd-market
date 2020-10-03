package engine.enums;

public enum DiscountOfferType {
    IRRELEVANT("Irrelevant"), ONE_OF("One of"), ALL_OR_NOTHING("All or Nothing");

    private final String value;

    DiscountOfferType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
