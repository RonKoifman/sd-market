package engine.models.user;

import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.interfaces.Identifiable;
import engine.interfaces.Transferable;

import java.util.Objects;

public abstract class User implements Identifiable, Transferable<UserDTO> {

    protected final int id;
    protected final String username;
    protected final UserRole userRole;
    private static int idGenerator = 1000;

    public User(String username, UserRole userRole) {
        this.id = idGenerator++;
        this.username = username;
        this.userRole = userRole;
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO.Builder()
                .username(username)
                .id(id)
                .userRole(userRole.getValue())
                .build();
    }

    @Override
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getUserRole() {
        return userRole;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
