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
    public void addNewAccount(String username) {
        usernameToUserAccount.put(username, new Account());
    }

    @Override
    public void deposit(String executorUsername, float transactionAmount, LocalDate transactionDate) {
        Account executorUserAccount = usernameToUserAccount.get(executorUsername);
        Transaction newDepositTransaction = new Transaction(TransactionType.DEPOSIT, transactionDate, transactionAmount, executorUserAccount.getBalance());
        executorUserAccount.addNewTransaction(newDepositTransaction);
    }

    @Override
    public void charge(String chargedUsername, String chargingUsername, float transactionAmount, LocalDate transactionDate) {
        Account chargedUserAccount = usernameToUserAccount.get(chargedUsername);
        Account chargingUserAccount = usernameToUserAccount.get(chargingUsername);
        chargedUserAccount.addNewTransaction(new Transaction(TransactionType.CHARGE, transactionDate, transactionAmount, chargedUserAccount.getBalance()));
        chargingUserAccount.addNewTransaction(new Transaction(TransactionType.RECEIVE, transactionDate, transactionAmount, chargingUserAccount.getBalance()));
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
