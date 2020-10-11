package dto.models;

import java.util.Objects;

public abstract class UserDTO {

    protected final int id;
    protected final String username;
    protected final String userRole;

    public UserDTO(int id, String userRole, String username) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
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
