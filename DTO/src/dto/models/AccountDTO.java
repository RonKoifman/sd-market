package dto.models;

import java.util.Collections;
import java.util.List;

public class AccountDTO {

    private final float balance;
    private final List<TransactionDTO> transactions;

    private AccountDTO(Builder builder) {
        balance = builder.balance;
        transactions = builder.transactions;
    }

    public float getBalance() {
        return balance;
    }

    public List<TransactionDTO> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public static final class Builder {
        private float balance;
        private List<TransactionDTO> transactions;

        public Builder balance(float balance) {
            this.balance = balance;
            return this;
        }

        public Builder transactions(List<TransactionDTO> transactions) {
            this.transactions = transactions;
            return this;
        }

        public AccountDTO build() {
            return new AccountDTO(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "balance=" + balance +
                    ", transactions=" + transactions +
                    '}';
        }
    }
}
