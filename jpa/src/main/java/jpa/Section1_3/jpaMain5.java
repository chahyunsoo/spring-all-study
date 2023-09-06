//package jpa.Section1_3;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//
///**
// * Flush
// */
//public class jpaMain5 {
//    public static void main(String[] args) {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
//        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
//        transaction.begin(); //transaction 시작
//
//        try {
//            Member member = new Member(700L, "memberC");
//            entityManager.persist(member);
//            //그런데 Transaction이 커밋되기 전까지는 쿼리를 볼 수가 없다.
//            //미리 DB에 반영을 하고 싶으면, flush를 강제로 호출하여 이 시점에 insert 쿼리가 바로 나간다.
//            entityManager.flush();
//
//
//            //commit하는 시점에 DB에 insert 쿼리가 날라가게 된다.
//            System.out.println("--------------");
//            transaction.commit(); //위에서 저장하고 commit을 한다.
//
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
//
//
