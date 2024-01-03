package spring.lectureA_2;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.aspectj.weaver.ast.Or;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.lectureA_2.domain.Delivery;
import spring.lectureA_2.domain.Order;
import spring.lectureA_2.domain.OrderItem;

@SpringBootApplication
public class LectureA2Application {

	public static void main(String[] args) {
		SpringApplication.run(LectureA2Application.class, args);
	}

	@Bean //지연로딩 강제하는건 비추! -> 엔티티를 노출하지 말자
	Hibernate5JakartaModule hibernate5Module() {
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
//		hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5JakartaModule;

	}

}
