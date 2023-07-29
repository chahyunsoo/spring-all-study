package spring.lecture0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture0.repository.*;
import spring.lecture0.service.MemberService;

@Configuration
public class SpringConfig {

//    private DataSource dataSource;

//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }


//    @PersistenceContext 원래는 이렇게 받야도 됨.
//    private EntityManager entityManager;

//    @Autowired
//    public SpringConfig(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean //스프링 빈에 등록하라는 뜻이네??
//    public MemberService memberService() {
//        return new MemberService(memberRepository());  //그래서 이 로직을 호출을 해서 스프링 빈에 등록을 해줌
//    }


//    @Bean
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(entityManager);  //EntityManager가 필요했었다.
//    }


//    @Bean
//    public MemberController memberController() {
//        return new MemberController(memberService());
//    }
//    이런 느낌이지만 Controller는 클래스에 @Controller 어노테이션을 그대로 둬야 하기에
//    이 코드를 작성할 필요가 없다.
}



