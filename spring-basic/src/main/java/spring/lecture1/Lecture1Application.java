package spring.lecture1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lecture1Application {

	public static void main(String[] args) {
		try {
			SpringApplication.run(Lecture1Application.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
