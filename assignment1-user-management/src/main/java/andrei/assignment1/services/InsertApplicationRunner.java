package andrei.assignment1.services;

import andrei.assignment1.entities.User;
import andrei.assignment1.entities.enums.Role;
import andrei.assignment1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InsertApplicationRunner implements ApplicationRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Access the application context here
        System.out.println("Application is fully initialized. Executing insert method...");

        UserRepository userRepository = context.getBean(UserRepository.class);
        //a0856447-425c-4407-8a4c-66fc57afb587
        User testUser = new User();
        testUser.setId(UUID.fromString("a0856447-425c-4407-8a4c-66fc57afb587"));
        Optional<User> user = userRepository.findById(testUser.getId());
        if (user.isPresent()) {
            return;
        }

        User user1 = new User();
        user1.setId(UUID.fromString("68fe497b-a189-47a8-aa92-5406b7b58d7c"));
        user1.setUsername("client");
        user1.setName("Cristi");
        user1.setPassword("client");
        user1.setRole(Role.CLIENT);
        userRepository.save(user1);

        User user2 = new User();
        user2.setId(UUID.fromString("3407384a-e4a3-40ed-a424-5e40cd04560a"));
        user2.setUsername("client2");
        user2.setName("Mihai");
        user2.setPassword("client2");
        user2.setRole(Role.CLIENT);
        userRepository.save(user2);

        User user3 = new User();
        user3.setId(UUID.fromString("f5c25939-41db-464e-b21f-65d84f73a40f"));
        user3.setUsername("admin2");
        user3.setName("Radu");
        user3.setPassword("admin2");
        user3.setRole(Role.ADMINISTRATOR);
        userRepository.save(user3);

        User user4 = new User();
        user4.setId(UUID.fromString("a0856447-425c-4407-8a4c-66fc57afb587"));
        user4.setUsername("admin");
        user4.setName("Andrei");
        user4.setPassword("admin");
        user4.setRole(Role.ADMINISTRATOR);
        userRepository.save(user4);


    }
}