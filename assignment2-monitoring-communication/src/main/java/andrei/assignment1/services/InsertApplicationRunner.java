package andrei.assignment1.services;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("insertApplicationRunner")
public class InsertApplicationRunner implements ApplicationRunner {

    @Autowired
    public InsertApplicationRunner(ApplicationContext context) {
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
        userRepository.save(user1);

        User user2 = new User();
        user2.setId(UUID.fromString("3407384a-e4a3-40ed-a424-5e40cd04560a"));
        userRepository.save(user2);

        User user3 = new User();
        user3.setId(UUID.fromString("f5c25939-41db-464e-b21f-65d84f73a40f"));
        userRepository.save(user3);

        User user4 = new User();
        user4.setId(UUID.fromString("a0856447-425c-4407-8a4c-66fc57afb587"));
        userRepository.save(user4);


        DeviceRepository deviceRepository = context.getBean(DeviceRepository.class);
        Device device1 = new Device();
        device1.setId(UUID.fromString("37a4c47c-3a4a-4f74-87a6-4a21c64bc9a8"));
        device1.setAddress("address1");
        device1.setDescription("description1");
        device1.setMaxEnergyConsumption(100);
        device1.setUser(user1);
        deviceRepository.save(device1);

        Device device2 = new Device();
        device2.setId(UUID.fromString("7d955181-06c0-4b9a-b4f5-5f5efcbf957d"));
        device2.setAddress("address2");
        device2.setDescription("description2");
        device2.setMaxEnergyConsumption(1000);
        device2.setUser(user1);
        deviceRepository.save(device2);

        Device device3 = new Device();
        device3.setId(UUID.fromString("a8c68929-e75c-4b3f-a9f3-4eaff1e0466c"));
        device3.setAddress("address3");
        device3.setDescription("description3");
        device3.setMaxEnergyConsumption(5);
        device3.setUser(user1);
        deviceRepository.save(device3);

        Device device4 = new Device();
        device4.setId(UUID.fromString("8a1be4ff-25b6-4090-9a9d-3885b8c2eec7"));
        device4.setAddress("address4");
        device4.setDescription("description4");
        device4.setMaxEnergyConsumption(10);
        device4.setUser(user2);
        deviceRepository.save(device4);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}