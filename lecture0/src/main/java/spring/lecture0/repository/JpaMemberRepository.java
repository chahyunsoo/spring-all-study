package spring.lecture0.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import spring.lecture0.domain.Member;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;  //JPA는 EntityManager로 동작을 한다.
    /*
    * implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    * 이 라이브러리를 주입받았으니까 스프링 부트가 EntityManger라는 것을 생성을 해준다(현재 데이터베이스랑 연결을 다 해줌)
    * 그래서 만들어준 것을 injectino만 받으면 된다.
    * application.properties에서 설정했던 정보들이랑 데이터베이스 Connection정보들이랑 자동으로 합쳐서 스프링 부트가 EntityManager를 만들어준 것.
    * 이것은 내부적으로, 이전에 만들었던 Datasource나 이런 것들을 내부적으로 들고 있어서, DB랑 통신하는 것을 내부에서 다 처리를 한다.
    * 결론은 JPA를 쓸려면 EntityManager를 주입을 받아야 한다.
    * */

    @Autowired
    public JpaMemberRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
        /*
        * 이렇게 하면 JPA가 insert query 다 만들어서 DB에 집어넣고
        * id까지 setId 해준다.
        * */
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); //조회할 타입이랑,식별자 p.k값 넣어주면 조회가 된다. select문 나간다.
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        //id는 p.k여서 findById메소드 코드처럼 하면 되는데, name은 현재 p.k가 아니잖아
        List<Member> result = em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        Optional<Member> finalresult = result.stream().findAny();
        return finalresult;
    }

    @Override
    public List<Member> findAll() {
        //객체 지향 쿼리(JPQL)
        //객체를 대상으로 쿼리를 날리는 것
        /* JPQL 임시 코드:
        String desiredName = "Jane";
        String jpql = "select m from Member m where m.name = :name";
        List<Member> membersByName = entityManager.createQuery(jpql, Member.class)
                .setParameter("name", desiredName)
                .getResultList();
         :name에 desiredName값이 바인딩 됨.
         이렇게 하면 JPQL 쿼리 실행 시 :name이 desiredName 변수의 값으로 대체도ㅣ서
         where m.name = 'Jane' 으로 쿼리가 실행된다.
         */
        List<Member> result = em.createQuery("select m from Member m", Member.class) //Member Entity자체를 Select
                .getResultList();
        return result;
    }
}
