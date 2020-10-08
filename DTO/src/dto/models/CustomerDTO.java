package dto.models;

import java.awt.*;
import java.util.Objects;

public class CustomerDTO extends UserDTO {



    private CustomerDTO(Builder builder) {
        super(builder.id, builder.userRole, builder.username, builder.location);
    }

    public static final class Builder {

       private int id;
       private String username;
       private String userRole;
       private Point location;

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

        public Builder location(Point location) {
            this.location = location;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(this);
        }
    }
}
