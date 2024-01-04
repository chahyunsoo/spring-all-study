package spring.lecture1.discount;

import spring.lecture1.member.Grade;
import spring.lecture1.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private final int fixPrice = 1000; //1000원 할인 고정

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return fixPrice;
        } else return 0;
    }
}
