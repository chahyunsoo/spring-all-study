package spring.lecture1.beanfind;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture1.discount.DiscountPolicy;
import spring.lecture1.discount.FixDiscountPolicy;
import spring.lecture1.discount.RateDiscountPolicy;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회시 자식이 둘 이상 있으면, 중복 오류 발생")
    void testInheritanceBeanDuplicate() {
//        DiscountPolicy bean = annotationConfigApplicationContext.getBean(DiscountPolicy.class);
        Assertions.assertThrows(NoUniqueBeanDefinitionException.class,
                () -> annotationConfigApplicationContext.getBean(DiscountPolicy.class));
    }
    @Test
    @DisplayName("부모 타입으로 조회시 자식이 둘 이상 있으면, 빈 이름을 지정하면 됨")
    void testInheritanceBean() {
//        DiscountPolicy rateDiscountPolicy = annotationConfigApplicationContext.getBean("rateDiscountPolicy", DiscountPolicy.class);
//        System.out.println("rateDiscountPolicy = " + rateDiscountPolicy);
        DiscountPolicy fixDiscountPolicy = annotationConfigApplicationContext.getBean("fixDiscountPolicy", DiscountPolicy.class);
        System.out.println("fixDiscountPolicy = " + fixDiscountPolicy);

//        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
        assertThat(fixDiscountPolicy).isInstanceOf(FixDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회")
    void testInheritanceAllBeans() {
        Map<String, DiscountPolicy> beansOfType = annotationConfigApplicationContext.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for (String s : beansOfType.keySet()) {
            System.out.println("key = " + s + "value= " + beansOfType.get(s));
        }
    }
    @Test
    @DisplayName("부모 타입으로 모두 조회 - Object타입으로")
    void testInheritanceAllBeansByObjectType() {
        Map<String, Object> beansOfType = annotationConfigApplicationContext.getBeansOfType(Object.class);
        for (String s : beansOfType.keySet()) {
            System.out.println("key = " + s + "value= " + beansOfType.get(s));
        }
    }

    @Configuration
    static class TestConfig {

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }
        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }
    }
}
