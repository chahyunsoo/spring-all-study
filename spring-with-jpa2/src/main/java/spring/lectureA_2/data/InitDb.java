package spring.lectureA_2.data;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.lectureA_2.domain.*;
import spring.lectureA_2.domain.item.Book;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager entityManager;

        public void dbInit1() {
            Member member = createMember("userA","seoul","111-111","1234");
            entityManager.persist(member);

            Book book1 = createBook("book1",10000,100);
            entityManager.persist(book1);

            Book book2 = createBook("book2",20000,200);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "busan", "222-222", "4321");
            entityManager.persist(member);

            Book book1 = createBook("bookA",10000,100);
            entityManager.persist(book1);

            Book book2 = createBook("bookB",20000,200);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 3);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private static Member createMember(String userName, String city, String streetNumber, String zipCode) {
            Member member = new Member();
            member.setName(userName);
            member.setAddress(new Address(city,  streetNumber, zipCode));
            return member;
        }

        private static Book createBook(String bookName,int price,int stockQuantity) {
            Book book1 = new Book();
            book1.setName(bookName);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }
}
