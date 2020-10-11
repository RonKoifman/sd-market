package engine.managers;

import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.models.user.Customer;
import engine.models.user.StoreOwner;
import engine.models.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SDMUsersManager implements UsersManager {

    private static SDMUsersManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final Map<String, User> usernameToUser = new HashMap<>();

    private SDMUsersManager() {
    }

    public static UsersManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMUsersManager();
                }
            }
        }

        return instance;
    }

    @Override
    public synchronized void addUser(String username, UserRole userRole) {
        switch (userRole) {
            case STORE_OWNER:
                usernameToUser.put(username, new StoreOwner(username, userRole));
                break;

            case CUSTOMER:
                usernameToUser.put(username, new Customer(username, userRole));
                break;
        }
    }

    @Override
    public synchronized Set<UserDTO> getUsers() {
        return usernameToUser.values()
                .stream()
                .map(User::toUserDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isUserExists(String username) {
        return usernameToUser.keySet()
                .stream()
                .anyMatch(currentUsername -> currentUsername.toLowerCase().equals(username.toLowerCase()));
    }

    @Override
    public synchronized UserDTO getUserByUsername(String username) {
       return usernameToUser.get(username).toUserDTO();
    }

    static SDMUsersManager getUserManager() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMUsersManager();
                }
            }
        }

        return instance;
    }

    synchronized User getUserModelByUsername(String username) {
        return usernameToUser.getOrDefault(username, null);
    }
}