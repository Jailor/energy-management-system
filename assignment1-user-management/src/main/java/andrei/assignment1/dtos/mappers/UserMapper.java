package andrei.assignment1.dtos.mappers;

import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.entities.User;
import org.springframework.beans.BeanUtils;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static UserResponseDTO toUserResponseDTO(User user) {
        if(user.getId() != null){
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            BeanUtils.copyProperties(user, userResponseDTO);
            return userResponseDTO;
        }
        return new UserResponseDTO(user.getUsername(), user.getName(), user.getRole());
    }

    public static User toEntity(UserDTO userDTO) {
        if(userDTO.getId() != null){
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            return user;
        }
        return new User(userDTO.getUsername(), userDTO.getName(), userDTO.getPassword(), userDTO.getRole());
    }
}
