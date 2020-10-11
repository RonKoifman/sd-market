package dto.models;

public class AccountDTO {



    private AccountDTO(Builder builder) {

    }

    public static final class Builder {



        public AccountDTO build() {
            return new AccountDTO(this);
        }
    }
}
