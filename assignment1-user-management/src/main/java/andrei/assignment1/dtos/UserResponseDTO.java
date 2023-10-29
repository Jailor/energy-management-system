package andrei.assignment1.dtos;

import andrei.assignment1.entities.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDTO {

    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String name;
    private Role role;

    public UserResponseDTO(String username, String name, Role role) {
        this.username = username;
        this.name = name;
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDTO that = (UserResponseDTO) o;
        return username.equals(that.username)  &&
                role.toString().equals(that.getRole().toString()) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, name);
    }
}
