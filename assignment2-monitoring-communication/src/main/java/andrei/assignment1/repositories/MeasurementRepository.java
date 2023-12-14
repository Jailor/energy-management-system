package andrei.assignment1.repositories;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {
    List<Measurement> findByDeviceOrderByTimestampDesc(Device device);
}
