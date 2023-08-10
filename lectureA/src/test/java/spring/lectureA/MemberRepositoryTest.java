package spring.lectureA;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


//@RunWith(SpringRunner.class)  Junit5부터는 @SpringBootTest안에 @RunWith(SpringRunner.class)가 포함
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("MemberRepository save() 테스트")
    void saveTest() {
        //given
        Member member = new Member();
        member.setUsername("choi");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.findMember(saveId);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

}