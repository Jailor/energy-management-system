package andrei.assignment1.dtos.security;

import andrei.assignment1.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginDTOResponse {
    // json web token
    private String jsonWebToken;

    // user details
    private Role userRole;
    private String username;
    private String name;
    private UUID id;

    // error details
    private Boolean badCredentials;
    private Boolean usernameNotFound;
    private Boolean multipleUsersFound;

    public LoginDTOResponse(){
        this.badCredentials = false;
        this.usernameNotFound = false;
        this.multipleUsersFound = false;
    }
    public Boolean isValid(){
        return !this.badCredentials && !this.usernameNotFound && !this.multipleUsersFound;
    }
}
