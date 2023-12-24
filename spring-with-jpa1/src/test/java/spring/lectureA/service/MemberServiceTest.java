package spring.lectureA.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA.domain.Member;
import spring.lectureA.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired  MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("회원가입_테스트")
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("shin");

        //when
        Long joinId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findOneMember(joinId));

    }

    @Test()
    @DisplayName("중복_회원_예외")
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("soo");

        Member member2 = new Member();
        member2.setName("soo");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); //예외 발생!
        });
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }
//        fail("예외가 발생해야 한다"); //코드가 돌다가 여기로 오면 안됨.


    }




}