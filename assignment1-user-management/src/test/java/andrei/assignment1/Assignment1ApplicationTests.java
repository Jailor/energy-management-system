package andrei.assignment1;

import andrei.assignment1.dtos.UserDTO;
import andrei.assignment1.dtos.UserResponseDTO;
import andrei.assignment1.entities.User;
import andrei.assignment1.entities.enums.Role;
import andrei.assignment1.repositories.UserRepository;
import andrei.assignment1.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
@SpringBootTest(classes = Assignment1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Assignment1ApplicationTests {
	@Autowired
	private UserRepository userRepository;
	@Test
	void contextLoads() {
		System.out.println("Real developers test in production!");
		User user = new User();
		user.setName("Andrei4");
		user.setPassword("andrei4");
		user.setUsername("andrei4");
		user.setRole(Role.ADMINISTRATOR);
		userRepository.save(user);

		boolean found = false;
		List<User> users = userRepository.findAll();
		for (User u : users) {
			if (u.getUsername().equals("andrei4")) {
				found = true;
				break;
			}
		}
		assert found;
	}

}
