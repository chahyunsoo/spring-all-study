package spring.lectureA_2.repository;

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
}
