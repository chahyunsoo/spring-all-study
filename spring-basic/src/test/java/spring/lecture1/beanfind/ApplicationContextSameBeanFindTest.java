package spring.lecture1.beanfind;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture1.AppConfig;
import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.member.MemberRepository;
import spring.lecture1.member.MemberService;
import spring.lecture1.member.MemoryMemberRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextSameBeanFindTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(testConfigClass.class);

    @Configuration //test코드안에서만 쓸라고 만듬
    static class testConfigClass {
        @Bean
        public MemberRepository memberRepository_A() {
            return new MemoryMemberRepository();
        }
        @Bean
        public MemberRepository memberRepository_B() {
            return new MemoryMemberRepository();
        }

    }

    @Test
    @DisplayName("타입으로 조회시 동일한 타입이 둘 이상 이면 중복 오류가 발생")
    void duplicateBean() {
//        MemberRepository bean = annotationConfigApplicationContext.getBean(MemberRepository.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> annotationConfigApplicationContext.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회시 동일한 타입이 둘 이상 이면, 빈 이름을 직접 지정")
    void noneDuplicateBean() {
        MemberRepository memberRepository = annotationConfigApplicationContext.getBean("memberRepository_A",MemberRepository.class);
        assertThat(memberRepository).isInstanceOf(MemoryMemberRepository.class);
    }

    @Test
    @DisplayName("타입이 동일한 빈을 모두 조회")
    void findAllBeansByTypes() {
        Map<String, MemberRepository> beansOfType = annotationConfigApplicationContext.getBeansOfType(MemberRepository.class);
        for (String s : beansOfType.keySet()) {
            System.out.println("key = " + s + "value= " + beansOfType.get(s));
        }
        System.out.println("beansOfType= " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
    }
}
