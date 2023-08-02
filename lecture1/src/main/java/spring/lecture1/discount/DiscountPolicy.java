package spring.lecture1.discount;

import spring.lecture1.member.Member;

public interface DiscountPolicy {

    int discount(Member member, int price); //return 할인 대상 금액

}
