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
public class Assignment2Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Assignment2Application.class);
	}

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
		SpringApplication.run(Assignment2Application.class, args);
	}

}
