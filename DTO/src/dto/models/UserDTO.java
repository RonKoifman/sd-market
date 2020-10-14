package dto.models;

import java.util.Objects;

public abstract class UserDTO {

    protected final int id;
    protected final String username;
    protected final String userRole;
    protected final AccountDTO account;

    public UserDTO(int id, String userRole, String username, AccountDTO account) {
        this.id = id;
        this.username = username;
        this.userRole = userRole;
        this.account = account;
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

    public AccountDTO getAccount() {
        return account;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userRole='" + userRole + '\'' +
                ", account=" + account +
                '}';
    }
}
