package spring.loginblogsample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.loginblogsample.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByLoginId(String loginId);

    boolean existsByNickName(String nickname);

    Optional<Member> findByLoginId(String loginId);
}
