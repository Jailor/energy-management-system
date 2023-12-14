package andrei.assignment1.dtos.energy;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeasurementDTO {
    private long timestamp;
    private String device_id;
    private double measurement_value;
}
