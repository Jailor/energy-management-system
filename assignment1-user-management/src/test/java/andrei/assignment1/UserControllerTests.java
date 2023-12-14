package andrei.assignment1;

import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.entities.enums.Role;
import andrei.assignment1.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class UserControllerTests extends Assignment1ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // ... tests go here
    @Test
    void getUsersTest() throws Exception {
        List<UserResponseDTO> userList = new ArrayList<>();
        userList.add(new UserResponseDTO(UUID.randomUUID(), "andrei", "andrei", Role.ADMINISTRATOR));


        when(userService.findUsers()).thenReturn(userList);

        mockMvc.perform(get("/user-api/user")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NzU2NDY4OCwiZXhwIjoxNzI5MTAwNjg4fQ.Auly5bSQGuPEzU6tFzRbq5Kzo5aXQC7f81QWPkrsPRM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userList.size()))); // Use JsonPath for JSON response validation
    }

    @Test
    void insertUserTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setName("New User");
        userDTO.setPassword("password");
        userDTO.setRole(Role.ADMINISTRATOR);

        UUID userId = UUID.randomUUID();
        when(userService.insert(any(UserDTO.class))).thenReturn(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/user-api/user")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NzU2NDY4OCwiZXhwIjoxNzI5MTAwNjg4fQ.Auly5bSQGuPEzU6tFzRbq5Kzo5aXQC7f81QWPkrsPRM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + userId + "\"" ));
    }

    @Test
    void updateUserTest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updateduser");
        userDTO.setName("Updated User");
        userDTO.setPassword("updatedpassword");
        userDTO.setRole(Role.ADMINISTRATOR);
        UUID userId = UUID.randomUUID();
        userDTO.setId(userId);

        when(userService.update(any(UserDTO.class))).thenReturn(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(put("/user-api/user/" + userId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NzU2NDY4OCwiZXhwIjoxNzI5MTAwNjg4fQ.Auly5bSQGuPEzU6tFzRbq5Kzo5aXQC7f81QWPkrsPRM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + userId.toString() + "\""));
    }

    @Test
    void deleteUserTest() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.delete(userId)).thenReturn(userId);

        mockMvc.perform(delete("/user-api/user/" + userId.toString())
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NzU2NDY4OCwiZXhwIjoxNzI5MTAwNjg4fQ.Auly5bSQGuPEzU6tFzRbq5Kzo5aXQC7f81QWPkrsPRM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + userId.toString() + "\""));
    }
}