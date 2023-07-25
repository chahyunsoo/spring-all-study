package spring.lecture0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture0.controller.MemberController;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;
import spring.lecture0.service.MemberService;

@Configuration
public class SpringConfig {

    @Bean //스프링 빈에 등록하라는 뜻이네??
    public MemberService memberService() {
        return new MemberService(memberRepository());  //그래서 이 로직을 호출을 해서 스프링 빈에 등록을 해줌
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    /*
    @Bean
    public MemberController memberController() {
        return new MemberController(memberService());
    }
    이런 느낌이지만 Controller는 클래스에 @Controller 어노테이션을 그대로 둬야 하기에
    이 코드를 작성할 필요가 없다.
    */


}
