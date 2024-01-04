package spring.lecture1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.discount.FixDiscountPolicy;
import spring.lecture1.discount.RateDiscountPolicy;
import spring.lecture1.member.MemberRepository;
import spring.lecture1.member.MemberService;
import spring.lecture1.member.MemberServiceImpl;
import spring.lecture1.member.MemoryMemberRepository;
import spring.lecture1.order.OrderService;
import spring.lecture1.order.OrderServiceImpl;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
