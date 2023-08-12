package spring.lectureA.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.lectureA.domain.Order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOneOrder(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAllOrders(OrderSearch orderSearch) {}
}
