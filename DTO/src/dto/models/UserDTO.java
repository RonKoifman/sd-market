package dto.models;

import java.awt.*;
import java.util.Objects;

public abstract class UserDTO {

    protected final int id;
    protected final String userRole;
    protected final String username;
    protected final Point location;

    public UserDTO(int id, String userRole, String username, Point location) {
        this.id = id;
        this.userRole = userRole;
        this.username = username;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUsername() {
        return username;
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
