package jpa_example.jpa_shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Item {
    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PRICE")
    private int price;
    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    //Item이 어느 카테고리에 속해있는지 알고 싶으면
    @ManyToMany(mappedBy = "items")  //이건 연관관계 주인이 아니니까 mappedBy= ~~~
    private List<Category> categories = new ArrayList<>();
}
