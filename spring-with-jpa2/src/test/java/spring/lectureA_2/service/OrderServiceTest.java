package spring.lectureA_2.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.*;
import spring.lectureA_2.domain.item.Book;
import spring.lectureA_2.domain.item.Item;
import spring.lectureA_2.exception.NotEnoughStockException;
import spring.lectureA_2.repository.OrderRepository;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("상품주문")
    public void 상품주문() throws Exception {
        Member member = createMember();

        Book book = createBook("JPA 1권", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다",1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8,book.getStockQuantity());
    }

    @Test
    @DisplayName("상품주문_재고수량초과")
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("JPA 1권", 10000, 10);

        int orderCount = 11;

        //when
        Assertions.assertThrows(NotEnoughStockException.class, () ->
                orderService.order(member.getId(), item.getId(), orderCount));
    }

    @Test
    @DisplayName("주문 취소")
    public void 주문_취소() throws Exception {
        //given
        Member member = createMember();
        Book item = createBook("JPA 1권", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOneOrder(orderId);

        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다", 10, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("광명시", "광덕산로", "14244"));
        em.persist(member);
        return member;
    }

}