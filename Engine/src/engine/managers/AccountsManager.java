package engine.managers;

import dto.models.AccountDTO;
import engine.enums.TransactionType;

import java.time.LocalDate;

public interface AccountsManager {

    void addNewAccount(String username);

    void addNewTransaction(TransactionType transactionType, String username, float transactionAmount, LocalDate transactionDate);

    AccountDTO getAccountByUsername(String username);
}
