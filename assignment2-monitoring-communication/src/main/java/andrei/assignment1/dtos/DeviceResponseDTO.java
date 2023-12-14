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
public class DeviceResponseDTO {
    //@NotNull
    //    @AgeLimit(limit = 18)
    //    private int age;
    private UUID id;
    private String description;
    private String address;
    private Integer maxEnergyConsumption;
    private UUID userId;
    private UserResponseDTO user;

    public DeviceResponseDTO(String description, String address, Integer maxEnergyConsumption, UUID userId) {
        this.description = description;
        this.address = address;
        this.maxEnergyConsumption = maxEnergyConsumption;
        this.userId = userId;
        this.user = null;
    }

    public DeviceResponseDTO(String description, String address, Integer maxEnergyConsumption, UUID userId, UserResponseDTO user) {
        this.description = description;
        this.address = address;
        this.maxEnergyConsumption = maxEnergyConsumption;
        this.userId = userId;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceResponseDTO deviceDTO = (DeviceResponseDTO) o;
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
