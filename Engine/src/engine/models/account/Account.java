package engine.models.account;

import dto.models.AccountDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Account {

    private float balance;
    private final List<Transaction> transactions = new LinkedList<>();

    public AccountDTO toAccountDTO() {
        return new AccountDTO.Builder()
                .balance(balance)
                .transactions(transactions.stream().map(Transaction::toTransactionDTO).collect(Collectors.toList()))
                .build();
    }

    public void addNewTransaction(Transaction newTransaction) {
        transactions.add(newTransaction);
        balance = newTransaction.getBalanceAfter();
    }

    public float getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
