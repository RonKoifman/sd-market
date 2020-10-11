package dto.models;

public class CustomerDTO extends UserDTO {



    private CustomerDTO(Builder builder) {
        super(builder.id, builder.userRole, builder.username);
    }

    public static final class Builder {

       private int id;
       private String username;
       private String userRole;

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

        public CustomerDTO build() {
            return new CustomerDTO(this);
        }
    }
}
