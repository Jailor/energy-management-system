package andrei.assignment1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Assignment3Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Assignment3ApplicationTests {
	@Test
	void contextLoads() {
		System.out.println("Real developers test in production!");
	}

}
