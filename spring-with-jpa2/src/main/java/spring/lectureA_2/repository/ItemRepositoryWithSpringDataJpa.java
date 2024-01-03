package spring.lectureA_2.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.domain.item.Item;

import java.util.Optional;

@Repository
public interface ItemRepositoryWithSpringDataJpa extends JpaRepository<Item,Long> {
    @Query("select it from Item it where it.id = :id")
    Optional<Item> findOne(Long id);
}
