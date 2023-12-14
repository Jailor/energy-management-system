package andrei.assignment1;

import andrei.assignment1.entities.Device;
import andrei.assignment1.entities.User;
import andrei.assignment1.repositories.DeviceRepository;
import andrei.assignment1.repositories.UserRepository;
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
@SpringBootTest(classes = Assignment1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Assignment1ApplicationTests {
	@Autowired
	private DeviceRepository deviceRepository;
	@Test
	void contextLoads() {


	}

}