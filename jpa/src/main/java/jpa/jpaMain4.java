package jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 변경 감지
 */
public class jpaMain4 {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
        transaction.begin(); //transaction 시작

        try {
            //일단 현재 DB에 p.k가 200인 데이터의 값이 memberA라고 되어 있는데, 이것을 변경해보겠다.
            Member member = entityManager.find(Member.class, 200L);
            member.setName("changedmemberAA");

//            entityManager.persist(member);
//            다시 persist할 필요가 없다.


            //commit하는 시점에 DB에 insert 쿼리가 날라가게 된다.
            transaction.commit(); //위에서 저장하고 commit을 한다.
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();

    }
}

