package spring.lectureA.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA.domain.Member;
import spring.lectureA.repository.MemberRepository;
import java.util.List;


@Service
@Transactional(readOnly = true)
//@AllArgsConstructor //필드 모든걸 가지고 생성자를 만들어줌
@RequiredArgsConstructor //final이 붙은 필드만 가지고 생성자를 만들어줌, injection에서 생성자에서 세팅하고 끝나는 필드들은 final로 잡음
public class MemberService {

    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final 권장

//    @Autowired  //생성자가 하나만 있는 경우에는 스프링이 자동으로 주입해줌,생략 가능
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입, 그 전에 중복 체크 먼저
     *
     * @param member
     * @return
     */
    @Transactional //join은 쓰기 전용, readOnly=true 로 하면 안됨.
    public Long join(Member member) {
        checkValidateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증, 같은 이름의 회원은 중복 회원 가입으로 간주한다고 가정.
     * @param member
     * @return
     */
    public void checkValidateMember(Member member) {
        List<Member> findMembers = memberRepository.findMemberByName(member.getName());  //중복성, UNIQUE 제약조건 권장
//        if (findMembers.size() > 0) { throw new IllegalStateException("중복되는 회원이 존재합니다.");}  //조금 더 최적화
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("중복되는 회원이 존재합니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAllMembers();
    }

    //회원 단건 조회
    public Member findOneMember(Long memberId) {
        return memberRepository.findOneMember(memberId);
    }

}

