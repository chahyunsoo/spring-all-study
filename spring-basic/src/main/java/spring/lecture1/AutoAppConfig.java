package spring.lecture1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import spring.lecture1.member.MemberRepository;
import spring.lecture1.member.MemoryMemberRepository;

@Configuration
@ComponentScan(
        basePackages = "spring.lecture1.member", //member패키지안에 있는 것들만 컴포넌트 스캔이 된다.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    public MemoryMemberRepository memoryMemberRepository() {
        return new MemoryMemberRepository();
    }
}
