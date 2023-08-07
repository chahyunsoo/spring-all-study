package spring.lecture1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.lecture1.member.*;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        이렇게 되면 memberServiceImpl()객체가
//        생성이 되면서 그 안에 MemoryMemberRepository 구현 객체가 딱 들어간다.
//        결국, memberService에는 memberServiceImpl이 들어감

//        MemberService memberService = new MemberServiceImpl();  객체를 직접 넣어준 방식...

        //ApplicationContext가 스프링 컨테이너라고 보면 된다.
        //얘가 모든 @Bean을 다 관리해준다.
        //이렇게 하면 AppConfig에 있는 환경 설정 정보를 가지고 AppConfig 클래스에 있는 @Bean이 붙은 메소드들을 스프링 컨테이너에 객체 생성한 것들을 다 집어넣어서 관리해주는 것이다.
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //그래서 원래는 AppConfig를 통해서 직!접! 찾아왔지만, 이제는 '스프링 컨테이너'를 통해서 찾아와야 한다.
        //아래 코드는 이름은 memberService이고 타입은 MemberService이다 라는 것을 명시하고 찾으면 된다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        System.out.println("memberService = " + memberService);


        Member member1 = new Member(1L, "choi", Grade.VIP);

        //회원가입 진행
        memberService.join(member1);
        Member findMember = memberService.findMember(1L);

        //새로 가입한 것과 가입하고 조회했을 때 나온 거랑 비교
        System.out.println("member1 = " + member1.getName());
        System.out.println("findMember = " + findMember.getName());
    }
}
