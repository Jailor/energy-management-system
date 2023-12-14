package andrei.assignment1.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageDTO {
    private String id;
    private String title;
    private String userId;
    private String position;
    private String type;
    private String text;
    private String dateString;
}
