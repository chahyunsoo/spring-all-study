package spring.lectureA_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.lectureA_2.domain.Order;

@Repository
public interface OrderRepositoryWithSpringDataJpa extends JpaRepository<Order,Long> {

}
