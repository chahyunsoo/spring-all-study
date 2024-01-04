package spring.lecture1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.discount.RateDiscountPolicy;
import spring.lecture1.member.*;
import spring.lecture1.order.Order;
import spring.lecture1.order.OrderService;
import spring.lecture1.order.OrderServiceImpl;

import java.util.Deque;

public class OrderApp {
    public static void main(String[] args) {
//        MemberService memberService = new MemberServiceImpl();
//        일단 회원을 메모리에 넣어놔야 주문해서 찾아쓸 수 있으니까
//        OrderService orderService = new OrderServiceImpl();
        
//        순수하게 AppConfig 클래스를 이용한 것
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();
        
//        스프링 빈을 이용한 것
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class); //이름, 타입
        OrderService  orderService = applicationContext.getBean("orderService", OrderService.class);


        Long memberId = 1L; String name="han";
        Member member = new Member(memberId, name, Grade.VIP);

        //일단 저장소에 저장해야 함.
        memberService.join(member);

        String itemName = "toy"; int itemPrice=20000;
        Order order = orderService.createOrder(memberId, itemName, itemPrice);
        System.out.println("order = " + order.toString());
        System.out.println("calculatePrice = " + order.calculatePrice());
    }
}
