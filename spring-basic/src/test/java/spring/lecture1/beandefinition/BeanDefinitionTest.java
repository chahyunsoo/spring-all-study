package spring.lecture1.beandefinition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import spring.lecture1.AppConfig;

public class BeanDefinitionTest {

    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    /*
    ApplicationContext genericXmlApplicationContext = new GenericXmlApplicationContext("appConfig.xml");
    .getBeanDefinition()을 사용하려면 반환타입이 ApplicationContext이면 안된다..
    */
//    GenericXmlApplicationContext annotationConfigApplicationContext = new GenericXmlApplicationContext("appConfig.xml");


    @Test
    @DisplayName("BeanDefinition 확인(빈 설정 메타 정보 확인)")
    void beanDefinition() {
        String[] beanDefinitionNames = annotationConfigApplicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition =  annotationConfigApplicationContext.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName + "beanDefinition = " + beanDefinition);
            }
        }
    }
}

