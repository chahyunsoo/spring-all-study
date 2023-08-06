package spring.lecture1.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.lecture1.AppConfig;

import java.util.Arrays;
import java.util.Iterator;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 조회 후 출력")
    void findAllBeans() {
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        /*
        Iterator<String> itr = Arrays.stream(beanDefinitionNames).iterator();
        while (itr.hasNext()) {
            String beanDefinitionName = itr.next();
            Object bean = annotationConfigApplicationContext.getBean(beanDefinitionName);
            System.out.println("beanDefinitionName = " + beanDefinitionName + ", bean = " + bean);
        }
        */
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = annotationConfigApplicationContext.getBean(beanDefinitionName); //bean에 반환된 객체가 저장됨
            System.out.println("빈 이름 = " + beanDefinitionName + "빈 객체= " + bean);
        }
    }
    @Test
    @DisplayName("애플리케이션 빈 조회 후 출력")
    void findApplicationAllBeans() {
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = annotationConfigApplicationContext.getBeanDefinition(beanDefinitionName);

            //Role ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
            //Role ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = annotationConfigApplicationContext.getBean(beanDefinitionName);
                System.out.println("빈 이름 = " + beanDefinitionName + "빈 객체= " + bean);
            }

        }


    }
}
