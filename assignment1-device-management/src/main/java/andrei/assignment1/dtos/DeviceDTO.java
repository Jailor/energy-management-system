package andrei.assignment1.dtos;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeviceDTO extends RepresentationModel<DeviceDTO> {
    private UUID id;
    private String description;
    private String address;
    private Integer maxEnergyConsumption;
    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return description.equals(deviceDTO.description) &&
                address.equals(deviceDTO.address) &&
                Objects.equals(maxEnergyConsumption, deviceDTO.maxEnergyConsumption)
                && userId.equals(deviceDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, address, maxEnergyConsumption, userId);
    }
}
