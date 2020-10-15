package engine.managers;

import dto.models.AccountDTO;

import java.time.LocalDate;

public interface AccountsManager {

    void addNewAccount(String username);

    void deposit(String executorUsername, float transactionAmount, LocalDate transactionDate);

    void charge(String chargedUsername, String chargingUsername, float transactionAmount, LocalDate transactionDate);

    AccountDTO getAccountByUsername(String username);
}
