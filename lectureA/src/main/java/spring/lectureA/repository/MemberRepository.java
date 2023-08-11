package spring.lectureA.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.lectureA.domain.Member;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
//    @Autowired
//    EntityManager는 Autowired로 안되고 PersistenceContext로 injection이 된다.
//    그런데 스프링 부트가 Autowired도 injectino이 가능하게 지원을 해준다.
    private final EntityManager em;

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    //회원 등록
    public void save(Member member) {
        em.persist(member); //영속성 컨텍스트에 Member객체를 넣는다,나중에 Transaction이 커밋되는 시점에 DB에 반영을 함(DB에 insert 쿼리가 날라감)
    }

    //회원 조회[단건으로 조회]
    public Member findOneMember(Long id) {
        return em.find(Member.class, id);
    }

    //회원 조회[리스트로 조회]
    public List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //회원 조회[리스트로 조회,by name], 파라미터 바인딩해서 특정 이름에 의해 찾음
    public List<Member> findMemberByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
