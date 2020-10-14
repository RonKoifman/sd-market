package dto.models;

public class StoreOwnerDTO extends UserDTO {



    private StoreOwnerDTO(Builder builder) {
        super(builder.id, builder.userRole, builder.username, builder.account);
    }

    public static final class Builder {

        private int id;
        private String username;
        private String userRole;
        private AccountDTO account;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder userRole(String userRole) {
            this.userRole = userRole;
            return this;
        }

        public Builder account(AccountDTO account) {
            this.account = account;
            return this;
        }

        public StoreOwnerDTO build() {
            return new StoreOwnerDTO(this);
        }
    }

    @Override
    public String toString() {
        return "StoreOwnerDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userRole='" + userRole + '\'' +
                ", account=" + account +
                '}';
    }
}
