package spring.lectureA_2;

import org.aspectj.weaver.ast.Or;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.lectureA_2.domain.Delivery;
import spring.lectureA_2.domain.Order;
import spring.lectureA_2.domain.OrderItem;

@SpringBootApplication
public class LectureA2Application {

	public static void main(String[] args) {
		SpringApplication.run(LectureA2Application.class, args);
	}
}
