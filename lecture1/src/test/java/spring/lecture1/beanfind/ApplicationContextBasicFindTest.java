package spring.lecture1.beanfind;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.lecture1.AppConfig;
import spring.lecture1.member.MemberService;
import spring.lecture1.member.MemberServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = annotationConfigApplicationContext.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);
        //getClass()는 해당 객체의 런타임 시의 실제 클래스 정보를 알고 싶을 때 사용
        System.out.println("memberService.getClass() = " + memberService.getClass());
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);  //memberService의 객체 인스턴스가 어떤 것이냐?
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanBy_Type() {
        MemberService memberService = annotationConfigApplicationContext.getBean(MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);  //memberService의 객체 인스턴스가 어떤 것이냐?
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanBy_Type2() {
        MemberServiceImpl memberService = annotationConfigApplicationContext.getBean("memberService",MemberServiceImpl.class);  //하지만 구체 클래스에 의존하는 방식은 좋은 코드는 아님.
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByNameX() {
//        annotationConfigApplicationContext.getBean("xxxxx", MemberService.class);
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> annotationConfigApplicationContext.getBean("xxxxx", MemberService.class));
    }
}
