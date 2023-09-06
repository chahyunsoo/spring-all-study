//package jpa.Section1_3;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//
///**
// * 변경 감지
// */
//public class jpaMain6 {
//    public static void main(String[] args) {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
//        //EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만든다.
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction(); //모든 데이터를 변경하는 모든 작업은 transaction을 얻어야 한다.
//        transaction.begin(); //transaction 시작
//
//        try {
//
//            Member member = entityManager.find(Member.class, 200L);
//            System.out.println("--------------");
//            member.setName("rerechangedmemberAA");
//
////            entityManager.detach(member);
//            entityManager.clear();
////            entityManager.close();
//
//            //영속성 컨테스트를 초기화하고 다시 조회한다면?
//            Member member2 = entityManager.find(Member.class, 200L);
//            member2.setName("newMemberAA");
//            //이러면 update 쿼리가 날라간다.
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
//
//
