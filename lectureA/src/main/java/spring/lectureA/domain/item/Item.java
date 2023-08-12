package spring.lectureA.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import spring.lectureA.domain.Category;
import spring.lectureA.exception.NoEnoughStockException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    /**
     * 재고 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int resultStock = this.stockQuantity - quantity;
        if (resultStock < 0) {
            throw new NoEnoughStockException("need more stock");
        }
        this.stockQuantity = resultStock;
    }
}
