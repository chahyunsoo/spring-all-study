package spring.lectureA_2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.domain.Order;
import spring.lectureA_2.domain.item.Item;
import spring.lectureA_2.repository.OrderSearch;
import spring.lectureA_2.service.ItemService;
import spring.lectureA_2.service.MemberService;
import spring.lectureA_2.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count); //일단 한번에 하나의 상품만 주문하게 함. 추후에 여러개 상품을 한번에 주문할 수 있게 변경 예정

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
//        model.addAttribute("orderSearch", orderSearch); //@ModelAttribute("orderSearch")OrderSearch orderSearch는 이 코드가 생략되있는 것.

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long cancelOrderId) {
        orderService.cancelOrder(cancelOrderId);

        return "redirect:/orders";
    }
}
