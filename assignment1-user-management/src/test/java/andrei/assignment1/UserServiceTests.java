package andrei.assignment1;

import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.entities.User;
import andrei.assignment1.entities.enums.Role;
import andrei.assignment1.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@AutoConfigureMockMvc
public class UserServiceTests extends Assignment1ApplicationTests {
    @Autowired
    UserService userService;

    @Test
    public void testGetCorrect() {
        // 'c8a78092-cfa8-4a36-924b-45ed8aafe303', 'admin3', 'Andrei3', 'admin3', 'ADMINISTRATOR'
        List<UserResponseDTO> userResponseDTOList = userService.findUsers();
        boolean found = false;
        for (UserResponseDTO user : userResponseDTOList) {
            if(user.getUsername().equals("admin3")){
                assertEquals("Test Insert User", "Andrei3", user.getName());
                found = true;
                break;
            }
        }
        assert found;
    }

    @Test
    public void testInsertCorrectWithGetById() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setName("New User");
        userDTO.setPassword("password");
        userDTO.setRole(Role.ADMINISTRATOR);
        User insertedUser = userService.insertUserLocal(userDTO);
        UserResponseDTO userResponseDTO = userService.findUserById(insertedUser.getId());
        assertEquals("Test Inserted User", "New User", userResponseDTO.getName());
    }

    @Test
    public void testInsertUpdate(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser2");
        userDTO.setName("New User2");
        userDTO.setPassword("password2");
        userDTO.setRole(Role.ADMINISTRATOR);
        User insertedUser = userService.insertUserLocal(userDTO);
        UserResponseDTO userResponseDTO = userService.findUserById(insertedUser.getId());
        assertEquals("Test Inserted User", "New User2", userResponseDTO.getName());
        userDTO.setName("New User 3");
        userDTO.setId(insertedUser.getId());
        userService.update(userDTO);

        userResponseDTO = userService.findUserById(insertedUser.getId());
        assertEquals("Test Updated User", "New User 3", userResponseDTO.getName());
    }

    @Test
    public void testDelete(){
        // 'c8a78092-cfa8-4a36-924b-45ed8aafe303', 'admin3', 'Andrei3', 'admin3', 'ADMINISTRATOR'
        userService.deleteUserLocal(UUID.fromString("c8a78092-cfa8-4a36-924b-45ed8aafe303"));
        List<UserResponseDTO> userResponseDTOList = userService.findUsers();
        boolean found = false;
        for (UserResponseDTO user : userResponseDTOList) {
            if(user.getUsername().equals("admin3")){
                found = true;
                break;
            }
        }
        assert !found;
    }
}
