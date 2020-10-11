package engine.managers;

import dto.models.UserDTO;
import engine.enums.UserRole;

import java.util.Set;

public interface UsersManager {

    void addUser(String username, UserRole userRole);

    Set<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);
}
