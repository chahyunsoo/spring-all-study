package spring.lecture1.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.discount.FixDiscountPolicy;
import spring.lecture1.discount.RateDiscountPolicy;
import spring.lecture1.member.*;

@Component
public class OrderServiceImpl implements OrderService {

//    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    DIP지키기 위해서 인터페이스에만 의존하게 변경(->AppConfig)
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    //생성자를 통해 어떤 구현체가 주입될지는 오로지 AppConfig에서 결정한다
    //그래서 OrderServiceImpl은 '실행'에만 집중하면 된다
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMemberById = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMemberById, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트용: memberRepository에 무슨 객체가 들어있는지 반환
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
