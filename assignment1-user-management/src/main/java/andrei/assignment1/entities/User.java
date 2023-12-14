package andrei.assignment1.entities;

import andrei.assignment1.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_table")
public class User implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "andrei.assignment1.entities.CustomUuidGenerator")
    private UUID id;
    @Column(name = "username1", nullable = false, unique = true)
    private String username;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String name, String password,Role role){
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

}
