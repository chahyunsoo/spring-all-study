package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
        transaction.begin(); //transaction 시작

        try {
            /* 저장
            Member member = new Member();
            member.setId(2L);
            member.setName("member2 ");
            entityManager.persist(member);
            * */

            /* 조회
            Member findmember = entityManager.find(Member.class, 1L);
            System.out.println("findmember.getId() = " + findmember.getId());    
            System.out.println("findmember.getName() = " + findmember.getName());
            * */

            /* 삭제
            entityManager.remove(findmember);
            * */

            /* 수정, 다시 저장 안해도 됨, JPA를 통해서 엔티티를 가져오면 JPA가 관리를 한다, JPA가 트랜잭션안에서 커밋을 할때 값이 변경 되었는지 다 체크를 한다,
            트랜잭션 커밋하기 직전에 UPDATE 쿼리를 만들어서 날리고 트랜잭션이 커밋이 된다.
            findmember.setName("kane");
            * */

            //Member 객체를 대상으로 쿼리를 짠다.
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1)  //1번 부터
                    .setMaxResults(10)  //10개 가져와라.
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }
            

            transaction.commit(); //위에서 저장하고 commit을 한다.
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();

    }
}
