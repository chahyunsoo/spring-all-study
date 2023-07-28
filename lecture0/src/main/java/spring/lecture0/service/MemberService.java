package spring.lecture0.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lecture0.domain.Member;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

@Transactional  //JpaMemberRepository에서 데이터를 저장하고 변경하고 했기 때문에 @Transactional을 써줘야함.
/*
어노테이션을 사용하면, 메서드나 클래스에 트랜잭션을 적용할 수 있다.
어노테이션을 메서드에 적용하면 해당 메서드가 실행될 때 트랜잭션을 시작하고,
메서드가 성공적으로 완료되면 트랜잭션을 커밋(Commit)하고,
메서드에서 예외가 발생하면 트랜잭션을 롤백(Rollback)해서
이전 상태로 돌리는 기능을 제공함.
*/
public class MemberService {
    private final MemberRepository memberRepository;

    /*
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    회원가입
    단, 같은 이름이 있으면 안됨
    */
    public Long join(Member member) {
        checkDuplicatedMember(member);
//        Member save = memberRepository.save(member);  member같은 객체여서 굳이???이렇게??그냥 member매개변수 이용해
        memberRepository.save(member);
        System.out.println("멤버 저장 완료!!!");
        return member.getId();
    }

    //중복 체크하는 로직을 따로 메소드로 뺌, refactoring 진행함
    private void checkDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("동일한 이름을 가지고 있는 회원이 존재");

                });
        System.out.println("중복 체크 완료!!!");
    }

    //전체 멤버 조회
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    //멤버 한명 조회
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }


}
