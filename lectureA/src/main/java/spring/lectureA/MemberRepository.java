//package spring.lectureA;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MemberRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public Long save(Member member) {
//        em.persist(member);
//        return member.getId();
//    }
//
//    public Member findMember(Long id) {
//        Member member = em.find(Member.class, id);
//        return member;
//    }
//
//}
