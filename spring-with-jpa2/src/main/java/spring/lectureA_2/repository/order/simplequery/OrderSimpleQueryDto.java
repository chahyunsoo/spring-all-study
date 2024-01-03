package spring.lectureA_2.repository.order.simplequery;

import lombok.Data;
import spring.lectureA_2.domain.Address;
import spring.lectureA_2.domain.Order;
import spring.lectureA_2.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = /*order.getOrderId();*/ orderId;
        this.name = /*order.getMember().getName();*/ name; //LAZY 초기화
        this.orderDate = /*order.getOrderDate();*/ orderDate;
        this.orderStatus = /*order.getStatus();*/ orderStatus;
        this.address = /*order.getDelivery().getAddress();*/ address; //LAZY 초기화
    }
}
