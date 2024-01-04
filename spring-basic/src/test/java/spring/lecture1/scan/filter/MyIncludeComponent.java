package spring.lecture1.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent { //MyIncludeComponent이 붙은 것은 컴포넌트 스캔에 추가할 것이다.
}
