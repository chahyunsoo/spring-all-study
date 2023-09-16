package jpa_example.jpa_shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
@Table(name = "ORDERS")
public class Order {
    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;
    /**
     * @JoinColumn(name = "Delivery_Id")를 설정하면
     * 먼저, Order 테이블에 Delivery_Id라는 외래키 컬럼이 생성됩니다.
     * Order의 Delivery delivery 변수에 delivery 엔티티가 할당되면 Order를 저장할 때
     * delivery 엔티티의 PK를 외래키 컬럼인 Delivery_Id에 저장하게 됩니다.
     */

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "ORDER_DATE")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS")
    private OrderStatus orderStatus;


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 만약 주문할 때 배송지 정보를 넣고 싶다면
     * 연관관계 편의 메소드를 활용하면 된다.
     */
}
