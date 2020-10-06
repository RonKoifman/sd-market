package engine.models.user;

import engine.enums.UserRole;
import engine.models.location.Location;
import engine.models.zone.Zone;

import java.util.HashMap;
import java.util.Map;

public class StoreOwner extends User {

    private final Map<String, Zone> zoneNameToZone = new HashMap<>();

    public StoreOwner(UserRole userRole, int id, String username, Location location) {
        super(userRole, id, username, location);
    }
}
