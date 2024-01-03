package spring.lectureA_2.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import spring.lectureA_2.domain.Category;
import spring.lectureA_2.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

/**
 * 엔티티 자체가 해결할 수 있는 것들은 주로 엔티티안에 비즈니스 로직을 담는 것이 객체지향적 설계를 하는데 좋 -> 응집도 up
 * 해당 도메인만 사용하는 비즈니스 로직에 대한 메소드라면 해당 도메인 내에서 작성,
 * 여러 도메인을 사용하는 비즈니스 로직이라면 서비스 계층에 생성하면 코드의 응집도가 올라감.
 * 비즈니스 로직 메소드를 만드는 데 빌더 패턴, 정적 팩토리 메소드 같은 것들도 사용
 * setter 지양 -> 비즈니스 메소드, 빌더 패턴, 정적 팩토리 메소드
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();


    //==비즈니스 로직==//
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    } 
}