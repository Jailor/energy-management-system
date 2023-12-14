package andrei.assignment1;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@SpringBootTest(classes = Assignment2Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Assignment2ApplicationTests {
	@Autowired
	private DeviceRepository deviceRepository;

	@Test
	void contextLoads() {
		System.out.println("Real developers test in production!");
		Device device = new Device();
		device.setMaxEnergyConsumption(100);
		device.setDescription("description69");
		device.setAddress("address69");
		User user = new User();
		user.setId(UUID.fromString("c8a78092-cfa8-4a36-924b-45ed8aafe303"));
		device.setUser(user);
		device.setId(UUID.fromString("75608c39-ed00-4fac-90a0-da613371aadb"));
		deviceRepository.save(device);

		boolean found = false;
		List<Device> devices = deviceRepository.findAll();
		for (Device d : devices) {
			if (d.getAddress().equals("address69")) {
				found = true;
				break;
			}
		}
		assert found;
	}

}
