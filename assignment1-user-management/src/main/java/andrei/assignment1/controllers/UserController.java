package andrei.assignment1.controllers;



import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin
@RequestMapping(value = "/user-api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping()
//    public ResponseEntity<List<UserDTO>> getUsers() {
//        List<UserDTO> dtos = userService.findUsers();
//        for (UserDTO dto : dtos) {
//            Link personLink = linkTo(methodOn(UserController.class)
//                    .getUser(dto.getId())).withRel("personDetails");
//            dto.add(personLink);
//        }
//        return new ResponseEntity<>(dtos, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertUser(@Valid @RequestBody UserDTO userDetailsDTO) {
        UUID backId = userService.insert(userDetailsDTO);
        return new ResponseEntity<>(backId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") UUID userId) {
        UserResponseDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UUID> updateUser(@PathVariable("id") UUID userId, @Valid @RequestBody UserDTO userDetailsDTO) {
        userDetailsDTO.setId(userId);
        UUID backId = userService.update(userDetailsDTO);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteUser(@PathVariable("id") UUID userId) {
        UUID backId = userService.delete(userId);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }

    @GetMapping(value = "/access-secure-resource")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Secure resource accessed successfully!", HttpStatus.OK);
    }

}
