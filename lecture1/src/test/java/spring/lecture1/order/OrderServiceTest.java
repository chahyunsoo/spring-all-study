package spring.lecture1.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.lecture1.member.Grade;
import spring.lecture1.member.Member;
import spring.lecture1.member.MemberService;
import spring.lecture1.member.MemberServiceImpl;

public class OrderServiceTest {
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    @DisplayName("주문 서비스 테스트")
    void createOrder() {
        //given
        Long testMemberId = 1L;
        Member testMember = new Member(testMemberId, "cha", Grade.VIP);
        memberService.join(testMember);

        //when
        Order order = orderService.createOrder(testMemberId, "car", 10000);

        //then
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
        Assertions.assertThat(order.calculatePrice()).isEqualTo(9000);






    }
}
