package andrei.assignment1.services;

import andrei.assignment1.controllers.handlers.exceptions.model.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import andrei.assignment1.controllers.handlers.exceptions.model.ResourceNotFoundException;
import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.dtos.mappers.UserMapper;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.UserRepository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log
public class UserService {
    @Value("${custom.device-prefix}")
    private String devicePrefix;
    @Value("${custom.user-secret}")
    private String customSecret;
    @Value("${custom.monitoring-prefix}")
    private String monitoringPrefix;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warning("User with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserMapper.toUserResponseDTO(userOptional.get());
    }

    public User insertUserLocal(UserDTO userDTO){
        User user = UserMapper.toEntity(userDTO);
        List<User> userList = userRepository.findByUsername(user.getUsername());
        if(!userList.isEmpty()){
            throw new UsernameAlreadyExistsException(
                    "Username already exists",
                    HttpStatus.CONFLICT,
                    "user",
                    Collections.singletonList("Username must be unique")
            );
        }

        return userRepository.save(user);
    }

    @Transactional
    public UUID insert(UserDTO userDTO) {
        User user = insertUserLocal(userDTO);

        RestTemplate restTemplate = new RestTemplate();
        String url = devicePrefix + "device-api/device/user/" + user.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customSecret);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            log.warning("User with id " + user.getId() +  " could not be inserted into the device microservice");
            throw new ResourceNotFoundException("User with id: " + user.getId() + "in device microservice");
        }

        return user.getId();
    }

    public UUID update(UserDTO userDetailsDTO){
        User user = UserMapper.toEntity(userDetailsDTO);
        // check if user already exists
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isEmpty()) {
            log.warning("User with id " + user.getId() +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + user.getId());
        }
        user.setPassword(userOptional.get().getPassword());
        user = userRepository.save(user);
        return user.getId();
    }

    public void deleteUserLocal(UUID id){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warning("User with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UUID delete(UUID id){
        deleteUserLocal(id);

        RestTemplate restTemplate = new RestTemplate();
        String url = devicePrefix + "device-api/device/user/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(customSecret);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<>() {});
        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            log.warning("Devices for user with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        return id;
    }

}
