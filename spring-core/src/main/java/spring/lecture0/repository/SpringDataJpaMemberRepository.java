package spring.lecture0.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.lecture0.domain.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaMemberRepository<Member,Long>,MemberRepository {

    @Override
    Optional<Member> findByName(String name);
}