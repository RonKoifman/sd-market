package engine.models.user;

import dto.models.CustomerDTO;
import dto.models.UserDTO;
import engine.enums.UserRole;
import engine.interfaces.Locationable;
import engine.interfaces.Identifiable;
import engine.models.location.Location;
import engine.models.order.GeneralOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                .location(location)
                .build();
    }
}
