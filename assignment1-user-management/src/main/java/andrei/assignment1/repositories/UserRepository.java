package andrei.assignment1.repositories;

import andrei.assignment1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<User> findByUsername(String name);

    /**
     * Example: Write Custom Query
     */
//    @Query(value = "SELECT p " +
//            "FROM User p " +
//            "WHERE p.username = :name " +
//            "AND p.age >= 60  ")
//    Optional<User> findSeniorsByName(@Param("name") String name);

}
