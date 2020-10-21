package engine.managers;

import dto.models.AccountDTO;
import engine.enums.TransactionType;
import engine.models.account.Account;
import engine.models.account.Transaction;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SDMAccountsManager implements AccountsManager {

    private static SDMAccountsManager instance;
    private static final Object CREATION_CONTEXT_LOCK = new Object();
    private final Map<String, Account> usernameToUserAccount = new HashMap<>();

    private SDMAccountsManager() {
    }

    public static AccountsManager getInstance() {
        if (instance == null) {
            synchronized (CREATION_CONTEXT_LOCK) {
                if (instance == null) {
                    instance = new SDMAccountsManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void createNewAccountToUser(String username) {
        if (usernameToUserAccount.containsKey(username)) {
            throw new IllegalStateException("The user '" + username + "' already has an account.");
        }

        usernameToUserAccount.put(username, new Account());
    }

    @Override
    public void addNewTransactionToUser(TransactionType transactionType, String username, float transactionAmount, LocalDate transactionDate) {
        Account userAccount = usernameToUserAccount.get(username);
        userAccount.addNewTransaction(new Transaction(transactionType, transactionDate, transactionAmount, userAccount.getBalance()));
    }

    @Override
    public AccountDTO getAccountByUsername(String username) {
        return usernameToUserAccount.get(username).toAccountDTO();
    }

    @Override
    public String toString() {
        return "SDMAccountManager{" +
                "usernameToUserAccount=" + usernameToUserAccount +
                '}';
    }
}
