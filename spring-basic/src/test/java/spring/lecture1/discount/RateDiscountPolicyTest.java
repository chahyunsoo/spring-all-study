package spring.lecture1.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.lecture1.member.Grade;
import spring.lecture1.member.Member;

import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {
    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();
    int discountPercent = 10;

    @Test
    @DisplayName("정률 할인 로직 성공 테스트(VIP이면 10% 할인")
    void ApplyDiscountTo_VIP() {
        //given
        Member member = new Member(1L, "kwon", Grade.VIP);
        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("정률 할인 로직 실패 테스트(VIP이면 10% 할인X")
    void NoApplyDiscountTo_VIP() {
        //given
        Member member = new Member(2L, "cha", Grade.BASIC);
        //when
        int discount = discountPolicy.discount(member, 10000);
        //then
        assertThat(discount).isEqualTo(0);
    }
}