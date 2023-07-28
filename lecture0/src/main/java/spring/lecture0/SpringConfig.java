package spring.lecture0;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.lecture0.controller.MemberController;
import spring.lecture0.repository.JdbcMemberRepository;
import spring.lecture0.repository.JdbcTemplateMemberRepository;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;
import spring.lecture0.repository.*;
import spring.lecture0.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {


//    private DataSource dataSource;

//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;

//    private DataSource dataSource;

//    @PersistenceContext 원래는 이렇게 받야도 됨.
    private EntityManager entityManager;

//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
    @Autowired
    public SpringConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Bean //스프링 빈에 등록하라는 뜻이네??
    public MemberService memberService() {
        return new MemberService(memberRepository());  //그래서 이 로직을 호출을 해서 스프링 빈에 등록을 해줌
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(entityManager);  //EntityManager가 필요했었다.
    }

    /*
    @Bean
    public MemberController memberController() {
        return new MemberController(memberService());
    }
    이런 느낌이지만 Controller는 클래스에 @Controller 어노테이션을 그대로 둬야 하기에
    이 코드를 작성할 필요가 없다.
    */


}
