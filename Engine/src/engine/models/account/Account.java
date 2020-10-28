package engine.models.account;

import dto.models.AccountDTO;
import engine.interfaces.Transferable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Account implements Transferable<AccountDTO> {

    private float balance;
    private final List<Transaction> transactions = new LinkedList<>();

    @Override
    public AccountDTO toDTO() {
        return new AccountDTO.Builder()
                .balance(balance)
                .transactions(transactions.stream().map(Transaction::toDTO).collect(Collectors.toList()))
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
