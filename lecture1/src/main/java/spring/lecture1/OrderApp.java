package spring.lecture1;

import spring.lecture1.member.Grade;
import spring.lecture1.member.Member;
import spring.lecture1.member.MemberServiceImpl;
import spring.lecture1.member.MemberService;
import spring.lecture1.order.Order;
import spring.lecture1.order.OrderService;
import spring.lecture1.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L; String name="han";
        Member member = new Member(memberId, name, Grade.VIP);
        //일단 저장소에 저장해야 함.
        memberService.join(member);

        String itemName = "toy"; int itemPrice=10000;
        Order order = orderService.createOrder(memberId, itemName, itemPrice);
        System.out.println("order = " + order.toString());
        System.out.println("calculatePrice = " + order.calculatePrice());

    }
}
