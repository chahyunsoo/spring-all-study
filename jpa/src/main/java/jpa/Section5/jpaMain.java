package jpa.Section5;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
//            team.getMembers().add(member);  이것은 연관관계의 주인이 아닌 곳에 read가 아닌 행위를 한 것. 그래서 Member의 TEAM_ID가 null이 됨.
            em.persist(team); //영속 상태가 되면 무조건 p.k값이 설정이 되야 영속 상태가 된다.

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team); //연관관계의 주인이 곳에 값을 넣었기 때문에 정상적으로 Member의 TEAM_ID가 값이 들어 온다.
            em.persist(member);
//            team.getMembers().add(member);

            //member.setTeamId(team.getId()); // -> 뭔가 객체지향스럽지 않음, 이것은 f.k를 직접 다루는 상황

            em.flush();
            em.clear(); //영속성 컨텍스트 초기화 시키고 아래 코드에서 다시 find하면 select DDL 확인 가능

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}