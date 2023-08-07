package spring.lecture1.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    @DisplayName("stateful 테스트")
    void statefulServiceSingletonTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);
        //둘이 같은 참조값 임을 확인할 수 있다
        System.out.println("statefulService1 = " + statefulService1);
        System.out.println("statefulService2 = " + statefulService2);
        assertThat(statefulService1).isSameAs(statefulService2);


        /*
        * 2개의 고객의 요청이 왔다고 가정
        * USER_1이 주문을 하고 getPrice()를 통해 주문 금액을 조회 하려 하는데,
         * 그 순간 USER_2가 주문을 하는 상황
        * 그러면 USER_1이 주문 금액을 조회 하면 얼마가 나올까?  -->20000
        * 이러명 망함.
        * */

        //Thread 1: 1번 사용자가 10000원을 주문함
        statefulService1.order("USER_1", 10000);
        //Thread 2: 2번 사용자가 10000원을 주문함
        statefulService2.order("USER_2", 20000);

        //Thread 1: 1번 사용자 주문 금액을 조회함
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);


        assertThat(statefulService1.getPrice()).isEqualTo(20000);  //Thread 2가 바꿔 치기 하기 때문에,,, 망한 테스트

//        @Configuration 바이트 코드 조작 확인용
//        ApplicationContext ac1 = new AnnotationConfigApplicationContext(TestConfig.class);
//        TestConfig bean = ac1.getBean(TestConfig.class);
//        System.out.println("bean = " + bean.getClass());
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}