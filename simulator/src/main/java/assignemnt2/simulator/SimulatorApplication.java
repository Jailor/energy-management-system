package assignemnt2.simulator;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

@SpringBootApplication
public class SimulatorApplication {
	@Value("${custom.measurement-time-millis}")
	private Long measurementTimeMillis;
	@Value("${custom.measurements-per-hour}")
	private Long measurementsPerHour;
	@Value("${custom.initial-delay-millis}")
	private Long initialDelayMillis;
	private final Long realHourMillis = 3_600_000L;
	private Long deltaTimeMillis;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Bucharest"));
		SpringApplication.run(SimulatorApplication.class, args);
	}

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Bean
	public CommandLineRunner defaultSimulationRunner(@Value("${custom.default-device-id}") String specificDeviceId) {
		return args -> simulate(specificDeviceId);
	}

	private void simulate(String specificDeviceId) throws Exception {
		System.out.println("Sleeping for " + initialDelayMillis + " milliseconds...");
		Thread.sleep(initialDelayMillis);
		System.out.println("Starting simulation...");
		// calculate deltaTimeMillis. It represents how much the simulator needs
		// to add to the timestamp in order to simulate the measurementsPerHour.
		double measurementRatio =   (double) realHourMillis / (measurementsPerHour * measurementTimeMillis);
		if(measurementRatio < 1) {
			deltaTimeMillis = 0L;
		}
		else {
			deltaTimeMillis = (long) (measurementTimeMillis * (measurementRatio - 1));
		}

		System.out.println("Fake hour in millis: " + measurementsPerHour* measurementTimeMillis);
		System.out.println("Measurement ratio" + measurementRatio);
		System.out.println("Time to add to each of the " + measurementsPerHour + " measurements: " + deltaTimeMillis);

		// Read sensor data from CSV
		InputStream resource = getClass().getResourceAsStream("/sensor.csv");
		if (resource == null) {
			throw new RuntimeException("Could not find sensor.csv file");
		}
		String csvData = new BufferedReader(new InputStreamReader(resource))
				.lines().collect(Collectors.joining("\n"));

		// Split CSV data into lines
		String[] lines = csvData.split("\n");
		Long rollingDeltaTime = 0L;

		// Simulate sending data to the queue
		for (String line : lines) {
			String[] values = line.split(",");
			long timestamp = System.currentTimeMillis();
			timestamp += rollingDeltaTime;
			rollingDeltaTime += deltaTimeMillis;
			double measurementValue = Double.parseDouble(values[0]);


			System.out.println("Local date time real:" + LocalDateTime.now());
			LocalDateTime localDateTimeNew =
					LocalDateTime.ofInstant(Instant.ofEpochMilli((timestamp)), ZoneId.systemDefault());
			System.out.println("local date time of time stamp + delta time: " + localDateTimeNew);
			/*System.out.println("local date time hour: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.systemDefault()).truncatedTo(ChronoUnit.HOURS));*/

			// Create JSON message
			String jsonMessage = String.format(Locale.US,
					"{\"timestamp\": %d, \"device_id\": \"%s\", \"measurement_value\": %.2f}",
					timestamp, specificDeviceId, measurementValue);
			System.out.println(jsonMessage);


			// Send message to the queue
			amqpTemplate.convertAndSend("device-measurement-queue", jsonMessage);

			// Sleep for 10 minutes (600,000 milliseconds)
			Thread.sleep(measurementTimeMillis);
		}
	}
}
