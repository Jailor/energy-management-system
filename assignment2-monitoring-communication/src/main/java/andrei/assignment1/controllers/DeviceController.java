package andrei.assignment1.controllers;



import andrei.assignment1.dtos.DeviceDTO;
import andrei.assignment1.dtos.DeviceResponseDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.HourlyConsumption;
import andrei.assignment1.services.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin
@RequestMapping(value = "/monitoring-api/device")
public class DeviceController {
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceResponseDTO>> getDevices() {
        List<DeviceResponseDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO userDetailsDTO) {
        UUID backId = deviceService.insert(userDetailsDTO);
        return new ResponseEntity<>(backId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceResponseDTO> getDevice(@PathVariable("id") UUID userId) {
        DeviceResponseDTO dto = deviceService.findDeviceById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UUID> updateDevice(@PathVariable("id") UUID userId, @Valid @RequestBody DeviceDTO userDetailsDTO) {
        userDetailsDTO.setId(userId);
        UUID backId = deviceService.update(userDetailsDTO);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID userId) {
        UUID backId = deviceService.delete(userId);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }

    // energy related: get the list of hourly consumptions for a device

    @GetMapping(value = "/hourly-consumption/{id}/{date}")
    public ResponseEntity<List<HourlyConsumption>> getHourlyConsumption(
            @PathVariable("id") UUID deviceId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<HourlyConsumption> dtos = deviceService.findHourlyConsumption(deviceId, date);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    // user related
    @GetMapping(value = "/user-devices/{id}")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesFromUser(@PathVariable("id") UUID userId) {
        List<DeviceResponseDTO> dtos = deviceService.findDevicesFromUser(userId);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user-devices/{id}")
    public ResponseEntity<UUID> deleteDevicesFromUser(@PathVariable("id") UUID userId) {
        UUID backId = deviceService.deleteDevicesFromUser(userId);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<UUID> deleteUser(@PathVariable("id") UUID userId) {
        deviceService.deleteUser(userId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @PostMapping(value = "/user/{id}")
    public ResponseEntity<UUID> insertUserId(@PathVariable("id") UUID userId) {
        UUID backId = deviceService.insertUserId(userId);
        return new ResponseEntity<>(backId, HttpStatus.OK);
    }


    @GetMapping(value = "/user")
    public List<UserResponseDTO> findUsers() {
        return deviceService.findUsers();
    }

}
