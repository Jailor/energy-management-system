package andrei.assignment1.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessageDTO {
    private UUID destinationUserId;
    private UUID sourceUserId;
    private String messageType;
    private MessageDTO message;
}
