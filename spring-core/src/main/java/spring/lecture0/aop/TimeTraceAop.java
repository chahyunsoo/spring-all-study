package spring.lecture0.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect  //이 어노테이션이 있어야 AOP로 쓸 수 있다.
//@Component //으로 해도 됨. 그런데 따로 설정 파일로 빼서 빈으로 등록시키는게 좋음
public class TimeTraceAop {

//    @Around("execution(* spring.lecture0..*(..))") //공통 관심사 targeting
//    @Around("execution(* spring.lecture0.service..*(..))") //service와 service 하위만 측정하고 싶을때
    @Around("execution(* spring.lecture0..*(..)) && !target(spring.lecture0.SpringConfig)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        //시간 로직
        long start = System.currentTimeMillis();
        System.out.println("START = " + joinPoint.toString());

        try{
//            Object result = joinPoint.proceed();  //다음 메소드로 진행
            return joinPoint.proceed();
        }finally {
            long end = System.currentTimeMillis();
            long timeDiff = end - start;
            System.out.println("END = " + joinPoint.toString() + " " + timeDiff + "ms");
        }
    }
}
