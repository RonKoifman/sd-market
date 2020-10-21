package engine.managers;

import dto.models.AccountDTO;
import engine.enums.TransactionType;

import java.time.LocalDate;

public interface AccountsManager {

    void createNewAccountToUser(String username);

    void addNewTransactionToUser(TransactionType transactionType, String username, float transactionAmount, LocalDate transactionDate);

    AccountDTO getAccountByUsername(String username);
}
