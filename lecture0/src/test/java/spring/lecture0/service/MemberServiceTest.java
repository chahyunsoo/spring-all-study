package spring.lecture0.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import  org.junit.jupiter.api.Test;
import spring.lecture0.domain.Member;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    /*MemberRepository memoryMemberRepository = new MemoryMemberRepository();
    * 이러면 cleanStore를 사용 못함, 타입을 바꿔줘야 함
    * */
    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }
    @Test
    @DisplayName("회원가입 Test")

    void join() {
        //given
        Member member = new Member();
        member.setName("son");

        //when
        Long joinId = memberService.join(member);

        //then
//        Optional<Member> one = memberService.findOne(joinId);

        Member findOneMember = memberService.findOne(joinId).get();
        assertThat(findOneMember.getName()).isEqualTo(member.getName());

//        assertThat(joinId).isEqualTo(member.getId()); ???????????? 이렇게 해도 되나???보류
    }

    @Test
    @DisplayName("중복 회원 예외 발생")
    public void joinException() {
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
        assertThat(resultMessage).isEqualTo("동일한 이름을 가지고 있는 회원이 존재");

        /*try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException illegalStateException) {
            String message = illegalStateException.getMessage();

            System.out.println(illegalStateException.getMessage()
                    .toString());
            assertThat(illegalStateException.getMessage())
                    .isEqualTo("동일한 이름을 가지고 있는 회원이 존재"); // O
            assertThat(illegalStateException.getMessage())
                    .isEqualTo("동일한 이름을 가지고 있는 회원이 존재1");// X
        }

//        ->try-catch로 나타내는것도 좋지만, 더 좋은 문법을 제공할걸?  -> assertThrows()
    }*/
    }
}