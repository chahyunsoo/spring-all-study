package spring.lectureA_2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {
    @Autowired MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional //변경
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다."); }
    }
    /**
     *전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * update메소드의 반환 값을 Member객체로 해도 되지만 '커맨드와 쿼리를 분리' 하자.
     * update는 변경성 메소드, Member를 반환형으로 두면 id로 조회하는 꼴이 된다. => 커맨드와 쿼리가 같이 있는 꼴
     * update 같은 것들은 가급적이면 그 안에서 끝내자.
     * 'CQS'패턴
     */
    @Transactional
    public void update(Long id, String name) {
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }
}

