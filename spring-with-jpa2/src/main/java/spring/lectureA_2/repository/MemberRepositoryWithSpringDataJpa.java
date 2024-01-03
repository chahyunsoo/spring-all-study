package spring.lectureA_2.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.lectureA_2.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepositoryWithSpringDataJpa extends JpaRepository<Member,Long> {
    @Query("select m from Member m where m.name = :name")
    List<Member> findByName(String name);

    @Query("select m from Member m where m.id = :id")
    Optional<Member> findOne(Long id);


//    Spring Boot 2.x 부터는 CrudRepository에 있는 findOne()이 없어지고 findById()가 추가됨.
//    findbyId()는 리턴타입이 Optional이기 때문에 get() 함수로 조회하면 됨.
}
