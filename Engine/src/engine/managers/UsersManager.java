package engine.managers;

import dto.models.UserDTO;
import engine.enums.UserRole;

import java.util.*;

public interface UsersManager {

    void addNewUser(String username, UserRole userRole);

    Set<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);
}
