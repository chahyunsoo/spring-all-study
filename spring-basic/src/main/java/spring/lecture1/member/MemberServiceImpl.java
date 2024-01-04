package spring.lecture1.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    DIP지키기 위해서 인터페이스에만 의존하게 변경(->AppConfig)
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트용: memberRepository에 무슨 객체가 들어있는지 반환
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}

