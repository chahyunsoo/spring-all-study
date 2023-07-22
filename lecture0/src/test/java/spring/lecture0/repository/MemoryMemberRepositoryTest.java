package spring.lecture0.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.lecture0.domain.Member;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    /*
    Test가 끝날때마다 데이터를 깔!끔!하게 지워주는 역할을 만들어주어야 한다.
     */
    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }



    @Test
    @DisplayName("save() 테스트")
    public void save() {
        Member member = new Member();
        member.setName("cha");

        memberRepository.save(member);
        Member result = memberRepository.findById(member.getId()).get();

//        Assertions.assertEquals(result,member); //기대하는건 저장한 member가 튀어나와야 함
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("findByName() 테스트")
    public void findByName() {
        Member member1 = new Member();
        member1.setName("cha");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("hyun");
        memberRepository.save(member2);

//        Optional<Member> result1 = memberRepository.findByName(member1.getName());
//        Optional<Member> result2 = memberRepository.findByName(member2.getName());

        //Optional 한번 까서 나타내봄
//        Member result1 = memberRepository.findByName(member1.getName()).get();
//        Member result2 = memberRepository.findByName(member2.getName()).get();
        Member result = memberRepository.findByName(member1.getName()).get();

//        assertThat(result1).isEqualTo(member1);
//        assertThat(result2).isEqualTo(member2);
        assertThat(result).isEqualTo(member1);
    }

    @Test
    @DisplayName("findAll() 테스트")
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setName("cha");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("hyun");
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
//        assertThat(result.size()).isEqualTo(3);  //F
    }
}
