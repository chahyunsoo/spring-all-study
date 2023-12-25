package spring.lectureA_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.lectureA_2.domain.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepositoryWithSpringDataJpa extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findOne(Long id);

}

