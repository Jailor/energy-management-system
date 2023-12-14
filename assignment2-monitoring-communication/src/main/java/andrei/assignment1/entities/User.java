package andrei.assignment1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_table")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private UUID id;
}
