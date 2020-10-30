package engine.models.notification;

import java.awt.*;

public class NewCompetitorNotification extends Notification {

    private final String competitorUsername;
    private final String regionName;
    private final String storeName;
    private final Point storeLocation;
    private final int amountOfItemsSoldByStore;
    private final int totalItemsInRegion;

    public NewCompetitorNotification(String regionName, String competitorUsername, String storeName, Point storeLocation, int amountOfItemsSoldByStore, int totalItemsInRegion) {
        super("New Competitor in Region");
        this.regionName = regionName;
        this.competitorUsername = competitorUsername;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.amountOfItemsSoldByStore = amountOfItemsSoldByStore;
        this.totalItemsInRegion = totalItemsInRegion;
        this.message = buildMessage();
    }

    public String getCompetitorUsername() {
        return competitorUsername;
    }

    public int getTotalItemsInRegion() {
        return totalItemsInRegion;
    }

    private String buildMessage() {
        return String.format("A new competitor arrived in your owned region '%s'!" + System.lineSeparator() +
                "The user '%s' opened a store named '%s' that locates in coordinates (%d, %d)." + System.lineSeparator() +
                "The store sells %d of the %d items available in the region.",
                regionName, competitorUsername, storeName, storeLocation.x, storeLocation.y, amountOfItemsSoldByStore, totalItemsInRegion);
    }

    @Override
    public String toString() {
        return "NewCompetitorNotification{" +
                "competitorUsername='" + competitorUsername + '\'' +
                ", regionName='" + regionName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeLocation=" + storeLocation +
                ", amountOfItemsSoldByStore=" + amountOfItemsSoldByStore +
                ", totalItemsInRegion=" + totalItemsInRegion +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
