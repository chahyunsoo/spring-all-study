package jpa_example.jpa_shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long orderItemId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "ITEM_ID")
    private Long itemID;
    private int orderPrice;
    private int count;

}
