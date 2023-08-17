package spring.lectureA_2.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.Address;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.domain.Order;
import spring.lectureA_2.domain.OrderStatus;
import spring.lectureA_2.domain.item.Book;
import spring.lectureA_2.domain.item.Item;
import spring.lectureA_2.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
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
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("광명시", "광덕산로", "14244"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA 1권");
        book.setPrice(30000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOneOrder(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
    }

    }