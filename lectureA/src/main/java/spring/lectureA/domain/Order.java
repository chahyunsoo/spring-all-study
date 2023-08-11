package spring.lectureA.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //Member와 관계를 세팅, orders와
    @JoinColumn  (name="member_id")  //mapping을 무엇으로 할 것이냐
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문의 상태 [ORDER,CANCEL]

    //==연관관계 편의 메소드==//
    //Member와 Order는 양방향
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);  //현재 Order 객체를 주어진 Member 객체의 orders 컬렉션에 추가하는 코드를 한번에 묶
    }

    //Order와 OrderItem도 양방향
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //Order와 Delivery도 양방향
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
