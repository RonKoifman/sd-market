package engine.models.user;

import dto.models.CustomerDTO;
import dto.models.UserDTO;
import engine.enums.UserRole;

public class Customer extends User {



    public Customer(String username, UserRole userRole) {
        super(username, userRole);
    }

    @Override
    public UserDTO toUserDTO() {
        return new CustomerDTO.Builder()
                .id(id)
                .username(username)
                .userRole(userRole.getValue())
                .build();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
