package spring.lectureA_2.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.Member;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired
    MemberRepositoryWithSpringDataJpa memberRepositoryWithSpringDataJpa;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() {
        Member member = new Member();
        member.setName("memberA");
        Long savedId = memberRepository.save(member);

        Member findMember = memberRepository.findOne(savedId);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());

        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
    }


    @Test
    @Transactional
    @Rollback(false)
    public void testMemberWithSpringDataJpa() {
        Member member = new Member();
        member.setName("memberB");

        Long savedId = memberRepositoryWithSpringDataJpa.save(member).getId();

        Optional<Member> findMember = memberRepositoryWithSpringDataJpa.findOne(savedId);

        Assertions.assertThat(findMember.get().getId()).isEqualTo(member.getId());

        Assertions.assertThat(findMember.get().getName()).isEqualTo(member.getName());

        System.out.println("findMember.get().getName() = " + findMember.get().getName());

    }
}

