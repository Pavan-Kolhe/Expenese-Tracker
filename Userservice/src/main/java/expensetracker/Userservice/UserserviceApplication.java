package expensetracker.Userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
@EnableKafka  // ADD THIS!  wasted 2 days in this
public class UserserviceApplication {

	public static void main(String[] args) {


		SpringApplication.run(UserserviceApplication.class, args);

	}
}