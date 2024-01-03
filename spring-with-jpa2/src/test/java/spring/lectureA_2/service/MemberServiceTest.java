package spring.lectureA_2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.exception.NotEnoughStockException;
import spring.lectureA_2.repository.MemberRepository;
import spring.lectureA_2.repository.MemberRepositoryWithSpringDataJpa;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
//    @Autowired MemberRepository memberRepository;
    @Autowired
    MemberRepositoryWithSpringDataJpa memberRepositoryWithSpringDataJpa;
    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long saveId = memberService.join(member);
        //then
        Member findMember = memberRepositoryWithSpringDataJpa.findOne(saveId).get();
        assertEquals(member, findMember);



//        System.out.println("member.getName() = " + member.getName());
//        System.out.println("memberRepositoryWithSpringDataJpa.getName() = " + findMember.getName());
    }
    
    @Test
    @DisplayName("회원가입_오류")
    public void 회원가입시_만약에_조회했는데_없을때() throws Exception {
        //given
        Member member = new Member();
        member.setName("king");
        Long join = memberService.join(member);
        //when
        Optional<Member> findOne = memberRepositoryWithSpringDataJpa.findOne(join);
        Member findMember;
        if (findOne.isPresent()) {
            findMember = findOne.get();
        } else throw new NoSuchElementException();

//        Member findMember = memberRepositoryWithSpringDataJpa.findOne(join)
//                .orElseThrow(() -> new RuntimeException("Member not found for ID: " + join));
        //then
        assertEquals(member, findMember);

        System.out.println("findMember.getName() = " + findMember.getName());
        System.out.println("member.getName() = " + member.getName());
        
    }
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        //when
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); //예외 발생!
        });
    }
}
