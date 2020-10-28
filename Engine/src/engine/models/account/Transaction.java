package engine.models.account;

import dto.models.TransactionDTO;
import engine.enums.TransactionType;
import engine.interfaces.Transferable;

import java.time.LocalDate;

public class Transaction implements Transferable<TransactionDTO> {

    private final TransactionType transactionType;
    private final LocalDate date;
    private final float amount;
    private final float balanceBefore;
    private final float balanceAfter;

    public Transaction(TransactionType transactionType, LocalDate date, float amount, float currentBalance) {
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        this.balanceBefore = currentBalance;
        this.balanceAfter = calculateBalanceAfterTransaction();
    }

    @Override
    public TransactionDTO toDTO() {
        return new TransactionDTO.Builder()
                .transactionType(transactionType.getValue())
                .date(String.format("%d/%d/%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear()))
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

    public float getBalanceAfter() {
        return balanceAfter;
    }

    private float calculateBalanceAfterTransaction() {
        float newBalance = 0;

        switch (transactionType) {
            case CHARGE:
                newBalance = balanceBefore - amount;
                break;

            case DEPOSIT:
            case RECEIVE:
                newBalance = balanceBefore + amount;
                break;
        }

        return newBalance;
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
