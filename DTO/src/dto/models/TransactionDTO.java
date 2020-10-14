package dto.models;

public class TransactionDTO {

    private final String transactionType;
    private final String date;
    private final float amount;
    private final float balanceBefore;
    private final float balanceAfter;

    private TransactionDTO(Builder builder) {
        this.transactionType = builder.transactionType;
        this.date = builder.date;
        this.amount = builder.amount;
        this.balanceBefore = builder.balanceBefore;
        this.balanceAfter = builder.balanceAfter;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDate() {
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

    public static final class Builder {

        private String transactionType;
        private String date;
        private float amount;
        private float balanceBefore;
        private float balanceAfter;

        public Builder transactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder amount(float amount) {
            this.amount = amount;
            return this;
        }

        public Builder balanceBefore(float balanceBefore) {
            this.balanceBefore = balanceBefore;
            return this;
        }

        public Builder balanceAfter(float balanceAfter) {
            this.balanceAfter = balanceAfter;
            return this;
        }

        public TransactionDTO build() {
            return new TransactionDTO(this);
        }
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "transactionType='" + transactionType + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}
