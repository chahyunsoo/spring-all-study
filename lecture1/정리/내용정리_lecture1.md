### 제어의 역전(IoC)
- 지금까지의 프로그램 흐름은 클라이언트 구현 객체가 스스로 필요한 구현 객체를 생성하고,연결하고,실행했다(구현 객체가 프로그램의 제어 흐름을 제어함)
- 하지만 AppConfig 라는 설정 클래스가 새로 생기면서 구현 객체는 자신의 로직을 실행하는 것에만 집중하게 되었다. 즉, 프로그램을 제어하는 힘이 AppConfig에게 생긴 것이다.
OrderServicImpl은 자기가 필요한 인터페이스를 호출하지만 어떤 구현 객체들이 들어올지는 모르는 상태인 것이다.
- OrderServicImpl도 AppConfig에 의해 생성된다. 이렇게 되면 OrderService 인터페이스에 대한 구현 객체를
OrderServiceImpl이 아닌 다른 구현 객체를 사용하길 원한다면, 바꾸면 된다. 이러한 사실들을 모른채 OrderServiceImpl은 자신의 서비스 로직만 묵묵히 실행한다.
- 이렇게 프로그램의 전반적인 흐름을 직접 제어하는 것이 아닌, 어떤 외부에서 관리하고 생성하고 하는 것을 '제어의 역전(IoC)'라고 한다.

### 의존관계 주입(Dependency Injection)

- 코드 상에서 OrderServiceImpl은 DiscountPolicy 인터페이스에 의존한다. 하지만 DiscountPolicy discountPolicy로 필드만 선언할 뿐
DiscountPolicy 인터페이스에 맞는 구현 객체를 직접 명시하지는 않았다.
- 의존 관계는 '정적인 클래스 의존 관계' 와 실행 시점에 결정되는 '동적인 객체(인스턴스) 의존 관계' 로 분리되서 생각할 수 있다고 한다.

그러면 이 두 의존 관계에 대해서 간략하게 알아보자.

<정적인 클래스 의존 관계>

이 의존 관계는 말그대로 정적인 관계이다. 서버를 실행 시키지 않더라도 import문을 통해 분석을 할 수 있다.
클래스 다이어그램이 이 예시이다.(소프트웨어 공학 수업에서 배움..)

![img.png](img.png)
이 그림을 보면 OrderServiceImpl은 MemberRepository에 의존하고 DiscountPolicy에도 의존하고 있다. 이것들은 인터페이스이며, 구현객체에 직접 의존하지 않아 DIP원칙을 잘 준수하고 있는것 같다.
하지만 이 의존관계들은 어디까지나 정적인 클래스 의존 관계이기 의존하고 있는 인터페이스에
어떤 구현 객체가 필요한지는 알 수 없다.

<동적인 객체 인스턴스 의존 관계>

- '실행 시점(런타임)'에 어떤 외부에서 구현 객체를 생성하고, 클라이언트에 전달하여
클라이언트와 서버의 실제 의존 관계가 연결되는 것을 '의존 관계 주입'이라고 한다.
('의존'이라는게 성질보다는 어떤 관계에 가깝기 때문에 의존 관계라는게 더 와닿는 다고
하는데, 나도 그렇게 생각한다,,?)
- 객체 인스턴스를 생성하고 그 참조값을 전달하여 연결되어 진다.
- 의존 관계 주입을 사용하면 클라이언트 코드(OrderServiceImpl)을 직접 바꾸지 않고, 클라이언트가 호출하는
대상의 타입 인스턴스를 변경할 수 있다.(FixDiscountPolicy->RateDiscountPolicy로 변경한 것 처럼)
- 의존 관계 주입을 사용하면 아까 위에서 봤던 정적 클래스 다이어그램의 의존 관계들을 변경하지 않고, 동적인 객체
인스턴스를 쉽게 변경할 수 있다.
---

지금까지 사용했던 AppConfig 클래스처럼 구현 객체들을 직접 생성해주고 관리하면서
의존 관계들을 주입 해주는 것들을 <b>`IoC 컨테이너`</b> 혹은 <b>`DI 컨테이너`</b>
라고 한다. 추가로 오브젝트 팩토리,어셈블러라고 부르기도 한다. 
---

### @Configuration
설정 정보 혹은 구성 정보를 담당하는 클래스(AppConfig)에 붙혀주는 어노테이션이다.


```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
}
```
(MemberApp 실행시 로그 중 일부)
```java
19:31:33.441 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.annotation.internalConfigurationAnnotationProcessor'
19:31:33.518 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
19:31:33.520 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
19:31:33.521 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
19:31:33.521 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
//위에 5개는 스프링이 내부적으로 필요해서 등록하는 스픠링 Bean
        
19:30:27.105 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'appConfig'
19:30:27.108 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'memberService'
19:30:27.111 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'memberRepository'
19:30:27.113 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'discountPolicy'
19:30:27.114 [main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'orderService'
//아래 5개는 @Bean 어노테이션을 통해 등록했던 스프링 Bean
```
추가로 Bean이 등록되는 형태를 보면,
```java
@Bean
public MemberService memberService() {
return new MemberServiceImpl(memberRepository());
}
```
이 코드를 예시로 하면, Key는 memberService Value는 new MemberServiceImpl 인스턴스이다.
그리고 꺼낼 때는 memberService 이름을 주고 꺼내면 된다.

### 스프링 컨테이너

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
OrderService  orderService = applicationContext.getBean("orderService", OrderService.class);
```

- ApplicationContext가 스프링 컨테이너이다.
- 기존에는 AppConfig를 통해서 직접 객체를 생성하고 DI를 했었지만, 이제는 스프링 컨테이너를 통해서 사용하게 되었다.
- 스프링 컨테이너는 `@Configuration`이 붙은 `AppConfig` 를 설정 정보로 사용한다.
여기서 `@Bean` 어노테이션이 붙은 메서드를 모두 호출해서 반환된 객체(EX. return new MemoryMemberRepository)를 스프링 컨테이너에 등록한다. 
이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
- 스프링 빈은 @Bean 어노테이션이 붙은 메서드의 명(Ex. memberService , orderService)을 스프링 빈의 이름으로 사용한다.
- 이전에는 개발자가 필요한 객체를 AppConfig 클래스를 통해서(AppConfig appConfig = new AppConfig(); MemberService memberService = appConfig.memberService();) 직접 조회했지만, 
이제부터는 스프링 컨테이너를 통해 필요한 스프링 빈(객체)를 찾아야 한다.
스프링 빈은 `applicationContext.getBean()` 메서드를 호출하여 찾을 수 있다.
- 그래서 결론은 기존에는 내가 직접 코드를 짜서 모든 것을 했는데, 이제는 스프링 컨테이너를 통해서 컨테이너에 스프링 빈을 
등록도 하고, 꺼내도 쓰고 할 수 있는 것이다.(스프링한테 환경 정보를 던져주고, 꺼낼때는 찾으면 된다)



 



