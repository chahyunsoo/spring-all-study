package spring.lectureA_2.repository;

import lombok.Getter;
import lombok.Setter;
import spring.lectureA_2.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
