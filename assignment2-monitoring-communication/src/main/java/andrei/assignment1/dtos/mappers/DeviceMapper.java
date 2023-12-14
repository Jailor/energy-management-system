package andrei.assignment1.dtos.mappers;

import andrei.assignment1.dtos.DeviceDTO;
import andrei.assignment1.dtos.DeviceResponseDTO;
import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import org.springframework.beans.BeanUtils;

public class DeviceMapper {

    private DeviceMapper() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();
        BeanUtils.copyProperties(device, deviceDTO);
        deviceDTO.setUserId(device.getUser().getId());
        return deviceDTO;
    }

    public static DeviceResponseDTO toDeviceResponseDTO(Device device) {
        if(device.getId() != null){
            DeviceResponseDTO deviceResponseDTO = new DeviceResponseDTO();
            BeanUtils.copyProperties(device, deviceResponseDTO);
            if(device.getUser() != null) {
                deviceResponseDTO.setUserId(device.getUser().getId());
            }
            return deviceResponseDTO;
        }

        if(device.getUser() != null) {
            return new DeviceResponseDTO(device.getDescription(), device.getAddress(),
                    device.getMaxEnergyConsumption(), device.getUser().getId());
        }
        else return new DeviceResponseDTO(device.getDescription(), device.getAddress(),
                device.getMaxEnergyConsumption(), null);

    }

    public static Device toEntity(DeviceDTO deviceDTO) {
        if(deviceDTO.getId() != null){
           Device device = new Device();
           BeanUtils.copyProperties(deviceDTO, device);
           if(deviceDTO.getUserId() != null){
               User user = new User();
               user.setId(deviceDTO.getUserId());
               device.setUser(user);
           }
           else device.setUser(null);

           return device;
        }
        if(deviceDTO.getUserId() != null) {
            User user = new User();
            user.setId(deviceDTO.getUserId());
            return new Device(deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMaxEnergyConsumption(), user);
        }
        else return new Device(deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getMaxEnergyConsumption(), null);
    }
}
