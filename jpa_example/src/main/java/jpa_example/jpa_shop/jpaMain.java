package jpa_example.jpa_shop;

import jpa_example.jpa_shop.domain.Member;
import jpa_example.jpa_shop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 데이터 중심 설계의 문제점
 * 1. 테이블의 외래키를 그대로 객체에 가져옴
 * 2. 객체 그래프 탐색이 불가능함
 * 3. 참조가 없음
 */
public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Order order = em.find(Order.class, 1L);
            Member member = order.getMember();


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
