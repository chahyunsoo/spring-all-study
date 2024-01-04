package spring.lecture0.repository;

import spring.lecture0.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    //Optional: null 처리하는 방식, 감싸서 반환함
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
