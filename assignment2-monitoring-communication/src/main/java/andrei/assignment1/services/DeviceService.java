package andrei.assignment1.services;

import andrei.assignment1.controllers.handlers.exceptions.model.ResourceNotFoundException;
import andrei.assignment1.dtos.DeviceDTO;
import andrei.assignment1.dtos.DeviceResponseDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.dtos.mappers.DeviceMapper;
import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.HourlyConsumption;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.repositories.HourlyConsumptionRepository;
import andrei.assignment1.repositories.UserRepository;
import lombok.extern.java.Log;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final HourlyConsumptionRepository hourlyConsumptionRepository;
    @Value("${custom.user-prefix}")
    private String userPrefix;
    @Value("${custom.jwt-token}")
    private String jwtToken;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository,
                         HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
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

    public UUID insert(DeviceDTO deviceDTO) {
        Device device = DeviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return device.getId();
    }

    public UUID update(DeviceDTO deviceDTO){
        Device device = DeviceMapper.toEntity(deviceDTO);
        // check if device already exists
        Optional<Device> userOptional = deviceRepository.findById(device.getId());
        if (userOptional.isEmpty()) {
            log.warning("Device with id " + device.getId() +  " was not found in db");
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + device.getId());
        }
        device = deviceRepository.save(device);
        return device.getId();
    }

    public UUID delete(UUID id){
        Optional<Device> userOptional = deviceRepository.findById(id);
        if (userOptional.isEmpty()) {
            log.warning("Device with id " + id +  " was not found in db");
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.deleteById(id);
        return id;
    }

    // energy related
    public List<HourlyConsumption> findHourlyConsumption(UUID deviceId, LocalDate date) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if(deviceOptional.isEmpty()){
            throw new ResourceNotFoundException("Device with id " + deviceId + " was not found in db");
        }
        // findByDeviceAndTimestampBetween(Device device, LocalDateTime start, LocalDateTime end);
        // we need to map localdate to localdatetime beginning and end of day
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return hourlyConsumptionRepository.findByDeviceAndTimestampBetween(deviceOptional.get(), start, end);
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

    //TODO : change this
    public UUID deleteDevicesFromUser(UUID userId) {
        List<Device> deviceList = deviceRepository.findByUserId(userId);
        for(Device device : deviceList){
           //device.setUserId(null);
           device.setUser(null);
        }
        deviceRepository.saveAll(deviceList);
        return userId;
    }

    public UUID deleteUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            log.warning("User with id " + userId +  " was not found in db");
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userId);
        }
        userRepository.deleteById(userId);
        return userId;
    }

    public UUID insertUserId(UUID userId) {
        if(userId != null){
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()) throw new ResourceNotFoundException("User with id " + userId + " is already present in db");
            // insert he user
            User user = new User();
            user.setId(userId);
            return userRepository.save(user).getId();
        }
        else {
            throw new ResourceNotFoundException("User function was supplied null id.");
        }
    }



}
