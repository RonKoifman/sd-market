package engine.managers;

import dto.models.UserDTO;
import engine.enums.TransactionType;
import engine.enums.UserRole;

import java.time.LocalDate;
import java.util.*;

public interface UsersManager {

    void addUser(String username, UserRole userRole);

    Set<UserDTO> getUsers();

    boolean isUserExists(String username);

    UserDTO getUserByUsername(String username);

    void addNewTransactionToUser(String username, float transactionAmount, LocalDate transactionDate, TransactionType transactionType);
}
