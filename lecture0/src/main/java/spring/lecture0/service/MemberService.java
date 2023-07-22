package spring.lecture0.service;

import spring.lecture0.domain.Member;
import spring.lecture0.repository.MemberRepository;
import spring.lecture0.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    회원가입
    단, 같은 이름이 있으면 안됨
    */
    public Long join(Member member) {
        checkDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //중복 체크하는 로직을 따로 메소드로 뺌, refactoring 진행함
    private void checkDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("동일한 이름을 가지고 있는 회원이 존재");
                });
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
