package dto.models;

public class UserDTO {

    private UserDTO(Builder builder) {

    }

    public static final class Builder {

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
