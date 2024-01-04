package spring.lecture0.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spring.lecture0.domain.Member;
import spring.lecture0.service.MemberService;

import java.util.List;

@Controller
public class MemberController {
    /*
    private final MemberService memberService = new MemberService();
    이렇게 직접 new로 객체를 생성해서 쓸 수도 있지만, 스프링이 관리하게 되면 스프링 컨테이너에 등록을 하고
    스프링 컨테이너로 부터 받아서 쓰도록 바꿔야 함. 지금 상황은 MemberController클래스 말고도 다른 기능을
    하는 Controller 클래스가 MemberService를 사용할 때 그때마다 인스턴스를 계속해서 생성해야 한다.
    */
    private final MemberService memberService;
    /*
    이렇게 하면 스프링이 처음 뜨고 MemberController 객체를 생성하며 생성자가 호출되는데,
    생성자에 @Autowired가 붙어있으면, 인자의 memberService를 스프링이 컨테이너에서
    꺼내서 연결을 시켜준다.
     */
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        System.out.println("memberService = " + memberService.getClass());  //proxy 객체인지 확인
    }
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm1";  //templates폴더에서 createMemberForm.html을 찾음
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getUsername());
        memberService.join(member);
//        System.out.println("member= " + member.getName());
        return "redirect:/";  //일단 보류, 다른 화면으로 리다이렉팅 예정
    }

    @GetMapping("/members/list")
    public String list(Model model) {
        List<Member> memberList = memberService.findAllMembers();
        model.addAttribute("memberList", memberList);
        return "members/memberList";

    }
}
