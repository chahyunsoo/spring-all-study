package jpa.Section5;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team); //영속 상태가 되면 무조건 p.k값이 설정이 되야 영속 상태가 된다.

            Member member = new Member();
            member.setUsername("member1");

            member.setTeam(team);

//            member.setTeamId(team.getId()); // -> 뭔가 객체지향스럽지 않음, 이것은 f.k를 직접 다루는 상황
            em.persist(member);
            em.flush();
            em.clear(); //영속성 컨텍스트 초기화 시키고 아래 코드에서 다시 find하면 select DDL 확인 가능

            //조회할때도 문제가 생김
            Member findMember = em.find(Member.class, member.getId());
//            Long teamId = findMember.getTeamId();
//            Team findTeam = em.find(Team.class, teamId);

            Team findTeam = findMember.getTeam();
            System.out.println("fi ndTeam.getName() = " + findTeam.getName());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}