package engine.models.user;

import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.interfaces.Identifiable;
import engine.interfaces.Locationable;
import engine.models.location.Location;

public abstract class User implements Locationable, Identifiable {

    private final UserRole userRole;
    private final int id;
    private final String username;
    private final Location location;
    private static int idGenerator;

    public User(UserRole userRole, int id, String username, Location location) {
        this.userRole = userRole;
        this.id = id;
        this.username = username;
        this.location = location;
    }

    public UserDTO toUserDTO() {
        return new UserDTO.Builder().build();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
