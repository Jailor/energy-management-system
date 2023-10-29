package andrei.assignment1.dtos;

import andrei.assignment1.entities.enums.Role;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO extends RepresentationModel<UserDTO> {
    private UUID id;
    private String username;
    private String name;
    private String password;
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return role.toString().equals(userDTO.getRole().toString()) &&
                Objects.equals(username, userDTO.username) && name.equals(userDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, name);
    }
}
