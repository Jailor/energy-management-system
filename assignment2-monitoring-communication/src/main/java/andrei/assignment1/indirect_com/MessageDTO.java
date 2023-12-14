package andrei.assignment1.indirect_com;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO implements Serializable {
    private String action;
    private String objectType;
    // id can be user or device
    private UUID id;
    // device fields, will be empty for user
    private String description;
    private String address;
    private Integer maxEnergyConsumption;
    private UUID userId;

    public MessageDTO(String action, String objectType, UUID id) {
        this.action = action;
        this.objectType = objectType;
        this.id = id;
    }
}