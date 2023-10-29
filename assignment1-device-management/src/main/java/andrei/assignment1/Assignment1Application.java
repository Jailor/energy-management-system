package andrei.assignment1;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import java.util.TimeZone;
import java.util.UUID;

@SpringBootApplication
public class Assignment1Application extends SpringBootServletInitializer {

	@Autowired
	ApplicationContext context;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Assignment1Application.class);
	}

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(Assignment1Application.class, args);
	}

	@PostConstruct
	public void init() {
		UserRepository userRepository = context.getBean(UserRepository.class);
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
		device1.setAddress("address1");
		device1.setDescription("description1");
		device1.setMaxEnergyConsumption(100);
		device1.setUser(user1);
		deviceRepository.save(device1);

		Device device2 = new Device();
		device2.setAddress("address2");
		device2.setDescription("description2");
		device2.setMaxEnergyConsumption(200);
		device2.setUser(user1);
		deviceRepository.save(device2);

		Device device3 = new Device();
		device3.setAddress("address3");
		device3.setDescription("description3");
		device3.setMaxEnergyConsumption(300);
		device3.setUser(user1);
		deviceRepository.save(device3);

		Device device4 = new Device();
		device4.setAddress("address4");
		device4.setDescription("description4");
		device4.setMaxEnergyConsumption(400);
		device4.setUser(user2);
		deviceRepository.save(device4);

	}

}
