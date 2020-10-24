package engine.enums;

public enum DiscountOfferType {
    IRRELEVANT("IRRELEVANT"), ONE_OF("ONE OF"), ALL_OR_NOTHING("ALL OR NOTHING");

    private final String value;

    DiscountOfferType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
