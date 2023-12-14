package andrei.assignment1.services;

import andrei.assignment1.controllers.handlers.exceptions.model.ResourceNotFoundException;
import andrei.assignment1.dtos.DeviceDTO;
import andrei.assignment1.dtos.DeviceResponseDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.dtos.mappers.DeviceMapper;
import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import andrei.assignment1.indirect_com.MessageDTO;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    @Value("${custom.user-prefix}")
    private String userPrefix;
    @Value("${custom.monitoring-prefix}")
    private String monitoringPrefix;
    @Value("${custom.jwt-token}")
    private String jwtToken;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public List<DeviceResponseDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        List<DeviceResponseDTO> responseDTOList = deviceList.stream().map(DeviceMapper::toDeviceResponseDTO)
                .toList();
        // associate the user to each device
        Map<UUID, UserResponseDTO> userResponseDTOMap = findUsersMap();
        for(DeviceResponseDTO dto : responseDTOList){
            dto.setUser(userResponseDTOMap.get(dto.getUserId()));
        }
        return responseDTOList;
    }

    public List<DeviceResponseDTO> findDevicesFromUser(UUID userId) {
        List<Device> deviceList = deviceRepository.findByUserId(userId);
        return deviceList.stream().map(DeviceMapper::toDeviceResponseDTO)
                .toList();
    }

    public DeviceResponseDTO findDeviceById(UUID id) {
        Optional<Device> userOptional = deviceRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warning("Device with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        // associate the user to the device
        DeviceResponseDTO dto =  DeviceMapper.toDeviceResponseDTO(userOptional.get());
        dto.setUser(findUserById(dto.getUserId()));
        return dto;
    }

    @Transactional
    public UUID insert(DeviceDTO deviceDTO) {
        Device device = DeviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        // send to monitoring
        deviceDTO.setId(device.getId());
        String url = monitoringPrefix + "monitoring-api/device";

      /*        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // Set the JWT token in the Authorization header

        HttpEntity<DeviceDTO> requestEntity = new HttpEntity<>(deviceDTO, headers);

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                UUID.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            log.warning("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
            throw new ResourceNotFoundException("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
        }*/
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setAction("insert");
        messageDTO.setObjectType("device");
        messageDTO.setId(device.getId());
        messageDTO.setAddress(device.getAddress());
        messageDTO.setDescription(device.getDescription());
        messageDTO.setMaxEnergyConsumption(device.getMaxEnergyConsumption());
        messageDTO.setUserId(device.getUser().getId());
        amqpTemplate.convertAndSend("update-queue", messageDTO.makeJSONString());

        return device.getId();
    }


    @Transactional
    public UUID update(DeviceDTO deviceDTO){
        Device device = DeviceMapper.toEntity(deviceDTO);
        // check if device already exists
        Optional<Device> userOptional = deviceRepository.findById(device.getId());
        if (userOptional.isEmpty()) {
            log.warning("Device with id " + device.getId() +  " was not found in db");
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + device.getId());
        }
        device = deviceRepository.save(device);
        // send to monitoring
      /*        String url = monitoringPrefix + "monitoring-api/device/" + device.getId();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // Set the JWT token in the Authorization header

        HttpEntity<DeviceDTO> requestEntity = new HttpEntity<>(deviceDTO, headers);

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                UUID.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            log.warning("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
            throw new ResourceNotFoundException("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
        }*/
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setAction("update");
        messageDTO.setObjectType("device");
        messageDTO.setId(device.getId());
        messageDTO.setAddress(device.getAddress());
        messageDTO.setDescription(device.getDescription());
        messageDTO.setMaxEnergyConsumption(device.getMaxEnergyConsumption());
        messageDTO.setUserId(device.getUser().getId());
        amqpTemplate.convertAndSend("update-queue", messageDTO.makeJSONString());
        return device.getId();
    }

    public UUID delete(UUID id){
        Optional<Device> userOptional = deviceRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warning("Device with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        // send to monitoring
     /*        String url = monitoringPrefix + "monitoring-api/device/" + id;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // Set the JWT token in the Authorization header

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UUID> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                UUID.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()){
            log.warning("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
            throw new ResourceNotFoundException("Monitoring service returned " + responseEntity.getStatusCode() + " status code");
        }*/
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setAction("delete");
        messageDTO.setObjectType("device");
        messageDTO.setId(id);
        amqpTemplate.convertAndSend("update-queue", messageDTO.makeJSONString());
        deviceRepository.deleteById(id);
        return id;
    }

    public List<UserResponseDTO> findUsers() {
        RestTemplate restTemplate = new RestTemplate();
        String url = userPrefix + "user-api/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // Set the JWT token in the Authorization header

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<UserResponseDTO>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});

        return responseEntity.getBody();
    }

    public Map<UUID, UserResponseDTO> findUsersMap(){
        List<UserResponseDTO> userResponseDTOList = findUsers();
        // make a hashmap of UUID -> UserResponseDTO
        Map<UUID, UserResponseDTO> map = new HashMap<>();
        for(UserResponseDTO dto : userResponseDTOList){
            map.put(dto.getId(), dto);
        }
        return map;
    }

    public UserResponseDTO findUserById(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = userPrefix + "user-api/user/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(userId)
                .toUri();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponseDTO> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                UserResponseDTO.class);

        return responseEntity.getBody();
    }

    @Transactional
    public UUID deleteUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.warning("User with id " + userId +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userId);
        }
        userRepository.deleteById(userId);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setAction("delete");
        messageDTO.setObjectType("user");
        messageDTO.setId(userId);
        messageDTO.setUserId(userId); // keep the json parser happy
        amqpTemplate.convertAndSend("update-queue", messageDTO.makeJSONString());

        return userId;
    }

    @Transactional
    public UUID insertUserId(UUID userId) {
        if(userId != null){
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()) throw new ResourceNotFoundException("User with id " + userId + " is already present in db");
            // insert he user
            User user = new User();
            user.setId(userId);
            user = userRepository.save(user);

            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setAction("insert");
            messageDTO.setObjectType("user");
            messageDTO.setId(userId);
            messageDTO.setUserId(userId); // keep the json parser happy
            amqpTemplate.convertAndSend("update-queue", messageDTO.makeJSONString());

            return user.getId();
        }
        else {
            throw new ResourceNotFoundException("User function was supplied null id.");
        }
    }



}
