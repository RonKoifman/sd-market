package engine.models.user;

import dto.models.StoreOwnerDTO;
import dto.models.UserDTO;
import engine.enums.UserRole;

public class StoreOwner extends User {

    public StoreOwner(String username, UserRole userRole) {
        super(username, userRole);
    }

    @Override
    public UserDTO toUserDTO() {
        return new StoreOwnerDTO.Builder()
                .id(id)
                .username(username)
                .userRole(userRole.getValue())
                .location(location)
                .build();
    }
}
