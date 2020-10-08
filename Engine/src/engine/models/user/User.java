package engine.models.user;

import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.interfaces.Identifiable;
import engine.interfaces.Locationable;
import engine.models.location.Location;

import java.util.Objects;

public abstract class User implements Locationable, Identifiable {

    protected final int id;
    protected final UserRole userRole;
    protected final String username;
    protected Location location;
    private static int idGenerator;

    public User(String username, UserRole userRole) {
        this.username = username;
        this.userRole = userRole;
        this.id = ++idGenerator;
    }

    public abstract UserDTO toUserDTO();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
