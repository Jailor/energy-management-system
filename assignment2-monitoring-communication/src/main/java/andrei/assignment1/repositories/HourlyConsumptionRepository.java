package andrei.assignment1.repositories;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, UUID> {
    //List<HourlyConsumption> findByDeviceOrderByTimestampDesc(Device device);
    Optional<HourlyConsumption> findByDeviceAndTimestamp(Device device, LocalDateTime timestamp);
    List<HourlyConsumption> findByDeviceAndTimestampBetween(Device device, LocalDateTime start, LocalDateTime end);
}
