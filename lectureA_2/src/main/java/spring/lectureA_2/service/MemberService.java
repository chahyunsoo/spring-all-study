package spring.lectureA_2.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.Member;
import spring.lectureA_2.repository.MemberRepository;
import spring.lectureA_2.repository.MemberRepositoryWithSpringDataJpa;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)  //조회 성능 최적, 읽기에는 readOnly=true , 쓰기에는 넣으면 안된다 , default는 readOnly=false
//@AllArgsConstructor //필드 모든걸 가지고 생성자를 만들어준다.
//@RequiredArgsConstructor //final이 붙은 필드를 가지고 생성자를 만들어준다.
public class MemberService {


    /**
     * 필드 주입
     */
//        @Autowired MemberRepository memberRepository;
//        @Autowired MemberRepositoryWithSpringDataJpa memberRepositoryWithSpringDataJpa;

    /**
     * setter 주입
     */
//        @Autowired
//        public void setMemberRepositoryWithSpringDataJpa(MemberRepositoryWithSpringDataJpa memberRepositoryWithSpringDataJpa) {
//             this.memberRepositoryWithSpringDataJpa = memberRepositoryWithSpringDataJpa;
//        }

    /**
     * 생성자 주입
     */
    private final MemberRepository memberRepository;
    @Autowired  //생성자가 1개만 있는 경우 @Autowired를 안써도 가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }


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

