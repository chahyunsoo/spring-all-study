package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

public class jpaMain2 {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
        transaction.begin(); //transaction 시작

        try {
            //비영속 상태
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("memberJPA");

            //영속 상태
//            entityManager.persist(member);

            /**
             * [1]. 영속 상태로 만들고 조회
             * [2]. 바로 조회 -> findMember1을 조회할때는 select 쿼리가 나가야 하고, findMember2를 조회할때는 select 쿼리가 나가면 안된다. 1차 캐시에서 가져와서..
             */
            //[1].
//            Member findmember = entityManager.find(Member.class, 101L);

            //[2].
            Member findmember1 = entityManager.find(Member.class, 101L);
            System.out.println("------------------------");
            Member findmember2 = entityManager.find(Member.class, 101L);
            System.out.println(findmember1 == findmember2);

            transaction.commit(); //위에서 저장하고 commit을 한다.
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();

    }
}
