package engine.models.account;

import dto.models.TransactionDTO;
import engine.enums.TransactionType;

import java.time.LocalDate;

public class Transaction {

    private final TransactionType transactionType;
    private final LocalDate date;
    private final float amount;
    private final float balanceBefore;
    private final float balanceAfter;

    public Transaction(TransactionType transactionType, LocalDate date, float amount, float balanceBefore, float balanceAfter) {
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public TransactionDTO toTransactionDTO() {
        return new TransactionDTO.Builder()
                .transactionType(transactionType.getValue())
                .date(date.toString())
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public float getBalanceBefore() {
        return balanceBefore;
    }

    public float getBalanceAfter() {
        return balanceAfter;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType=" + transactionType +
                ", date=" + date +
                ", amount=" + amount +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}
