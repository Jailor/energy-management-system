package andrei.assignment1.services;

import andrei.assignment1.dtos.energy.WebSocketMessageDTO;
import andrei.assignment1.entities.HourlyConsumption;
import andrei.assignment1.entities.Measurement;
import andrei.assignment1.repositories.HourlyConsumptionRepository;
import andrei.assignment1.repositories.MeasurementRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final HourlyConsumptionRepository hourlyConsumptionRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Value("${custom.hour-in-millis}")
    private long hourInMillis;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, HourlyConsumptionRepository hourlyConsumptionRepository,
                              SimpMessagingTemplate messagingTemplate) {
        this.measurementRepository = measurementRepository;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void processMeasurement(Measurement measurement) {
        // find the previous measurement for calculating the difference in energy consumption
        List<Measurement> measurementList = measurementRepository.findByDeviceOrderByTimestampDesc(measurement.getDevice());
        Measurement previousMeasurement;
        if(measurementList.isEmpty()){
            previousMeasurement = null;
        } else {
            previousMeasurement = measurementList.get(0);
            if(previousMeasurement.getTimestamp() > measurement.getTimestamp()){
                previousMeasurement = null;
                log.warning("Received invalid measurement");
                return;
            }
        }

        double energyConsumption;
        if(previousMeasurement != null){
            energyConsumption = measurement.getMeasurementValue() - previousMeasurement.getMeasurementValue();
        }
        else {
            energyConsumption = measurement.getMeasurementValue();
        }
        /*LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.systemDefault()).truncatedTo(ChronoUnit.HOURS));*/
        LocalDateTime hourTimestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(measurement.getTimestamp()),
                ZoneOffset.systemDefault()).truncatedTo(ChronoUnit.HOURS);
        Optional<HourlyConsumption> consumptionOptional =
                hourlyConsumptionRepository.findByDeviceAndTimestamp(measurement.getDevice(), hourTimestamp);
        HourlyConsumption hourlyConsumption;

        if(consumptionOptional.isEmpty()){
            hourlyConsumption = new HourlyConsumption();
            hourlyConsumption.setTimestamp(hourTimestamp);
            hourlyConsumption.setDevice(measurement.getDevice());
            hourlyConsumption.setHourlyConsumption(energyConsumption);
        } else {
            hourlyConsumption = consumptionOptional.get();
            hourlyConsumption.setHourlyConsumption(hourlyConsumption.getHourlyConsumption() + energyConsumption);
        }
        measurementRepository.save(measurement);
        hourlyConsumptionRepository.save(hourlyConsumption);

        // ws notification
        if(hourlyConsumption.getHourlyConsumption() > measurement.getDevice().getMaxEnergyConsumption()){
            String destination = "/topic";
            String message = "Hourly consumption exceeded for device " + measurement.getDevice().getDescription() +
                    " with id " + measurement.getDevice().getId()
                    + ". The hourly consumption is " + hourlyConsumption.getHourlyConsumption() +
                    " and the maximum allowed is " + measurement.getDevice().getMaxEnergyConsumption();
            WebSocketMessageDTO webSocketMessageDTO = new WebSocketMessageDTO();
            webSocketMessageDTO.setMessage(message);
            webSocketMessageDTO.setHourlyConsumption(hourlyConsumption.getHourlyConsumption());
            webSocketMessageDTO.setMaxEnergyConsumption(measurement.getDevice().getMaxEnergyConsumption());
            webSocketMessageDTO.setUserId(String.valueOf(measurement.getDevice().getUser().getId()));
            webSocketMessageDTO.setDeviceId(String.valueOf(measurement.getDevice().getId()));

            messagingTemplate.convertAndSend(destination, webSocketMessageDTO);
            log.info("Sent message to websocket: " + webSocketMessageDTO);
        }
    }

}
