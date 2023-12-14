package andrei.assignment1.dtos.energy;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WebSocketMessageDTO {
    private String message;
    private String deviceId;
    private int maxEnergyConsumption;
    private double hourlyConsumption;
    private String userId;
}
