package engine.api;

import dto.models.UserDTO;
import engine.enums.UserRole;

import java.util.Set;

public interface UserManager {

    void addUser(String username, UserRole userRole);

    Set<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);
}
