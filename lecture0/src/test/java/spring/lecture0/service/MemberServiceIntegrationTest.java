package spring.lecture0.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.lecture0.domain.Member;
import spring.lecture0.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;   //구현체는 SpringConfig에서 올라온다.

    @Test
    @DisplayName("회원가입 Test")
    void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("son");

        //when
        Long joinId = memberService.join(member);

        //then
        Member findOneMember = memberService.findOne(joinId).get();
        assertThat(findOneMember.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("중복 회원 예외 발생")
    void joinException() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("son");
        Member member2 = new Member();
        member2.setName("son");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
        String resultMessage = e.getMessage();

        //then
        assertThat(resultMessage).isEqualTo("동일한 이름을 가지고 있는 회원이 존재");
    }
}