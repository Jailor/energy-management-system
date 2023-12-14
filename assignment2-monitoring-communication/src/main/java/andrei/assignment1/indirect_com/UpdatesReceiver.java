package andrei.assignment1.indirect_com;

import andrei.assignment1.dtos.DeviceDTO;
import andrei.assignment1.services.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "update-queue")
@Component
@DependsOn("insertApplicationRunner")
@Log
public class UpdatesReceiver {
    private final ObjectMapper objectMapper;

    private final DeviceService deviceService;

    @Autowired
    public UpdatesReceiver(ObjectMapper objectMapper, DeviceService deviceService) {
        this.objectMapper = objectMapper;
        this.deviceService = deviceService;
    }

    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [x] Received in monitoring and communication microservice in update queue:\n'" + in );
        try {
            MessageDTO messageDTO = objectMapper.readValue(in, MessageDTO.class);

            System.out.println(" [x] Received in monitoring and communication microservice decoded:\n'" + messageDTO);

            if(messageDTO.getObjectType().equals("device")){
                DeviceDTO deviceDTO = new DeviceDTO();
                deviceDTO.setId(messageDTO.getId());
                deviceDTO.setAddress(messageDTO.getAddress());
                deviceDTO.setMaxEnergyConsumption(messageDTO.getMaxEnergyConsumption());
                deviceDTO.setDescription(messageDTO.getDescription());
                deviceDTO.setUserId(messageDTO.getUserId());
                switch (messageDTO.getAction()){
                    case "insert":
                        deviceService.insert(deviceDTO);
                        break;
                    case "update":
                        deviceService.update(deviceDTO);
                        break;
                    case "delete":
                        deviceService.delete(deviceDTO.getId());
                        break;
                    default:
                        System.err.println("Error parsing RabbitMQ message: " + messageDTO + "action not recognized");
                }
            }
            else if(messageDTO.getObjectType().equals("user")){
                switch (messageDTO.getAction()){
                    case "insert":
                        deviceService.insertUserId(messageDTO.getId());
                        break;
                    case "delete":
                        deviceService.deleteUser(messageDTO.getId());
                        break;
                    default:
                        System.err.println("Error parsing RabbitMQ message: " + messageDTO + "action not recognized");
                }
            }
            else {
                System.err.println("Error parsing RabbitMQ message: " + messageDTO + "object type not recognized");
            }
        } catch (Exception e) {
            System.err.println("Error parsing RabbitMQ message: " + e.getMessage());
        }
    }

}
