package spring.lecture1.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent { //MyExcludeComponent이 붙은 것은 컴포넌트 스캔에 제외할 것이다.
}
