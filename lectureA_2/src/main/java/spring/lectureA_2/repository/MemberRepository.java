package spring.lectureA_2.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import spring.lectureA_2.domain.Member;

import java.util.List;

@Repository
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberRepository {

    /**
     * EntityManager는 @Autowired가 아니라 @PersistenceContext가 있어야 injection이 된다.
     * 하지만 스프링 부트가 @Autowired도 injection 되게 지원을 해준다.
     */
//    @PersistenceContext
//    private final EntityManager em;

//    @Autowired
    private final EntityManager em;

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
