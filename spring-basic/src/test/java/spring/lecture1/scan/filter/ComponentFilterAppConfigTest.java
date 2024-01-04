package spring.lecture1.scan.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        IncludeBean includeBean = annotationConfigApplicationContext.getBean("includeBean", IncludeBean.class);
        Assertions.assertThat(includeBean).isInstanceOf(IncludeBean.class);
        Assertions.assertThat(includeBean).isNotNull();

//        ExcludeBean excludeBean = annotationConfigApplicationContext.getBean("excludeBean", ExcludeBean.class);
        //이걸 조회하는 순간 예외 발생! 없으니까, 제외했으니까
        //org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'excludeBean' available
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> annotationConfigApplicationContext.getBean("excludeBean", ExcludeBean.class)
        );

    }

    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
    )
    static class TestConfig {
    }

}
