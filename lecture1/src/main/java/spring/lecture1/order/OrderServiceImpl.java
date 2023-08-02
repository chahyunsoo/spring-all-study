package spring.lecture1.order;

import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.discount.FixDiscountPolicy;
import spring.lecture1.member.*;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member findMemberById = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMemberById, itemPrice);
//        int orderPrice = new Order(memberId, itemName, itemPrice, discountPrice)
//                .calculatePrice(itemPrice, discountPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
