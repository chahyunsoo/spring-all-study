package spring.lectureA.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import spring.lectureA.domain.item.Item;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  //주문 당시 가격

    private int count;  //주문 당시 수량




}
