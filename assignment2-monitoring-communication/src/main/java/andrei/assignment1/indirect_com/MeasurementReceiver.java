package andrei.assignment1.indirect_com;

import andrei.assignment1.dtos.energy.MeasurementDTO;
import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.Measurement;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.services.MeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RabbitListener(queues = "device-measurement-queue")
@Component
@DependsOn("insertApplicationRunner")
@Log
public class MeasurementReceiver {
    private final ObjectMapper objectMapper;
    private final MeasurementService measurementService;
    private final DeviceRepository deviceRepository;

    @Autowired
    public MeasurementReceiver(ObjectMapper objectMapper, MeasurementService measurementService, DeviceRepository deviceRepository) {
        this.objectMapper = objectMapper;
        this.measurementService = measurementService;
        this.deviceRepository = deviceRepository;
    }

    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [x] Received in monitoring and communication microservice in measurement queue:\n'" + in );
        try {
            MeasurementDTO measurementDTO = objectMapper.readValue(in, MeasurementDTO.class);
            //System.out.println(" [x] Received in monitoring and communication microservice decoded:\n'" + measurementDTO);
            Measurement measurement = new Measurement();
            measurement.setMeasurementValue(measurementDTO.getMeasurement_value());
            measurement.setTimestamp(measurementDTO.getTimestamp());
            Device device = deviceRepository.findById(UUID.fromString(measurementDTO.getDevice_id())).orElse(null);
            if(device == null){
                System.err.println("Device with id " + measurementDTO.getDevice_id() + " not found");
                return;
            }
            measurement.setDevice(device);
            measurementService.processMeasurement(measurement);

        } catch (Exception e) {
            System.err.println("Error parsing RabbitMQ message: " + e.getMessage());
        }
    }
}
