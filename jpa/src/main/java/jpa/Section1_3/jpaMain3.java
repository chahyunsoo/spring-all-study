//package jpa.Section1_3;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//
///**
// * 쓰기 지연
// */
//public class jpaMain3 {
//    public static void main(String[] args) {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
//        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
//        transaction.begin(); //transaction 시작
//
//        try {
//            //영속
//            Member memberA = new Member(200L, "memberA");
//            Member memberB = new Member(300L, "memberB");
//
//            entityManager.persist(memberA);
//            entityManager.persist(memberB);
//            System.out.println("---------------------");
//
//            //commit하는 시점에 DB에 insert 쿼리가 날라가게 된다.
//            transaction.commit(); //위에서 저장하고 commit을 한다.
//        } catch (Exception e) {
//            transaction.rollback();
//        } finally {
//            entityManager.close();
//        }
//
//        entityManagerFactory.close();
//
//    }
//}
