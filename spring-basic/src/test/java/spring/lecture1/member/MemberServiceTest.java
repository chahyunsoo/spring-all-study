package spring.lecture1.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.lecture1.AppConfig;

public class MemberServiceTest {
    //    MemberService memberService = new MemberServiceImpl();
    MemberService testMemberService;
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        testMemberService = appConfig.memberService();
    }

    @Test
    void join() {
        //given
        Member member = new Member(1L, "shin", Grade.VIP);

        //when
        testMemberService.join(member);
        Member findMember = testMemberService.findMember(1L);

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
