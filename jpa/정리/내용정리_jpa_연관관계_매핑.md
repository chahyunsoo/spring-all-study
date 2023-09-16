객체를 테이블에 맞추어서 모델링 하였을 때 Member 클래스의 teamId(외래키)가 Team 클래스의 id를 참조하는 상황
```java
public class Member {
    ...
    @Column(name = "TEAM_ID")
    private Long teamId; //외래키값을 그대로 사용했음   
}

public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    ...
}

public static void main(String[] args) {
    Team team = new Team();
    team.setName("TeamA");
    em.persist(team); //영속 상태가 되면 무조건 p.k값이 설정이 되야 영속 상태가 된다.

    Member member = new Member();
    member.setUsername("member1");
    member.setTeamId(team.getId()); // -> 뭔가 객체지향스럽지 않음, 이것은 외래키 식별자를 직접 다루는 상황
    em.persist(member);

    //조회할때도 문제가 생김
    Member findMember = em.find(Member.class, member.getId());
    Long teamId = findMember.getTeamId();
    Team findTeam = em.find(Team.class, teamId);    //식별자를 다시 조회하는데 사용하는 문제
}
```
위의 코드는 이런 문제가 있다.
- 외래키 식별자를 직접 다루는 상황
- 식별자를 다시 조회하는데 사용

DB 테이블은 외래키를 이용해서 조인을 한 후 서로 연관된 테이블의 값을 찾을 수 있다.
하지만, 객체는 참조를 해야 서로 연관된 객체를 찾을 수 있다. 

### 단방향 연관관계

```java
public class Member {
    ...
    @Column(name = "TEAM_ID")
    private Long teamId;

    이 코드를 아래와 같이 바꿈

    @ManyToOne  //Member 클래스 입장에서는 Many이므로 
    @JoinColumn(name = "TEAM_ID") //Team team과 실제 Member 테이블의 TEAM_ID(외래키)컬럼과 매핑을 해야하기 때문에  
    private Team team;
}
```

현재 Member와 Team은 N:1 관계이다.(하나의 팀에 여려명의 사람이 들어갈 수 있다는 조건하에) -> @ManyToOne
Team team과 실제 Member 테이블의 TEAM_ID(외래키)컬럼과 매핑을 해야하기 때문에 -> @JoinColumn(name = "TEAM_ID")

![img.png](img/img.png)

**객체의 참조와 DB의 외래키를 매핑을 해서 연관관계 매핑을 할 수 있다.**

### 양방향 연관관계

테이블의 경우 연관관계는 F.K로 양방향이 모두 존재한다. 테이블의 연관관계는 방향이랑 개념 자체가 없는 것!
F.K 이용해서 그냥 join하면 되니까 서로의 연관을 다 알 수 있다. 하지만 문제는 객체이다.

연관관계의 주인과 mappedBy -> 객체와 테이블간의 연관관계를 맺는 차이를 이해해야 함. 테이블은 F.K로 인해 양방향 관계처럼 보이지만,
객체는 서로가 단방향으로 관계를 맺는다. 즉, 객체의 양방향 관계는 진짜 양방향 관계이기 보다는 서로 다른 단방향 관계이다.
객체를 양방향 참조하려면 단방향 연관관계를 2개를 만들어야 한다. 테이블은 F.K 하나로 두 테이블의 연관관계를 관리한다.(양쪽으로 join)

```java
public class Member {
    ...
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}
public class Team {
    ...
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}
```
**Member 클래스의 team으로 F.K를 관리해야 할지 아니면 Team 클래스의 List members로 F.K를 관리해야 할지 그 주인을 정해야 한다.**
이 연관관계의 주인이라는 개념은 양방향 매핑일때 나온다.
- 객체의 두 관계중 하나를 연관관계의 주인으로 지정해야 한다.
- 그 연관관계의 주인만이 F.K를 관리한다.(등록, 수정)
- 주인이 아닌 것은 Only Read
- 주인이 아니면 mappedBy 속성으로 주인을 지정한다.
- 주인은 mappedBy 속성을 사용하면 안된다.

그러면 여기서 어떤 것을 주인으로 지정해야 할까??
> F.K가 있는 곳으로 주인을 정하자, 위의 코드에서는 Member.team이 연관관계의 주인이다.

DB 테이블로 따졌을 때 F.K가 있는 곳이 N, 이 쪽이 연관관계의 주인이 된다. 위에서는 Member : Team 이 N : 1 이니까 Member에 연관관계의 주인이 있다.

양방향 매핑시 가장 많이 하는 실수?

- 연관관계의 주인이 아닌 곳에 값을 넣음 -> 연관관계의 주인이 있는 클래스의 DB의 F.K값이 null이 된다.
```java
team.getMembers().add(member); //역방향(주인이 아닌 방향)만 연관관계 설정
```
- 그런데 양쪽에 모두 값을 넣지 않으면 위의 값이 없으면 jpa가 읽어들이지 못한다.
- 양방향 매핑시 순수한 객체를 고려해서 양쪽에 다 값을 입력 해주어야 하는게 맞다.
- 위의 코드를 대체하기 위하여 Member 클래스(연관관계의 주인이 있는 곳)에 연관관계 편의 메소드를 작성할 수 있다.
- 양방향 매핑시에 무한루프를 조심해야 한다. -> 컨트롤러에서 Entity를 바로 반환하면 Entity를 JSON으로 바꿀 때 장애가 날 수 있다.
  - 그래서 컨트롤러에는 Entity를 절대 반환하지 않고 DTO를 사용하여 Entity를 적절히 변환 후 반환해야 한다.

정리
- 양방향 매핑관계는 단방향 매핑만으로도 이미 연관관계는 매핑이 완료된 것이다.
- JPA 모델링을 할 때 일단 단뱡향 매핑으로 설계를 끝내야 한다.(양방향 매핑은 하지 않는다고 생각)
- 그래서 단방향 매핑을 우선 잘하고 양방향 매핑이 필요하면 추후에 고려해도 됨.
- 우선 단방향으로 끝낸 다음에 N:1 일때 N인 쪽에 연관관계를 다 매핑하고 설계를 끝낸 다음에 개발 단계에서 양방향 매핑을 고려해도 늦지 않다!
- 연관관계를 정하는 기준
  - 비즈니스 로직을 기준으로(이게 중요하니까 연관관계 주인으로 정하겠다!) 연관관계 주인을 정할 수 있지만 
  **연관관계의 주인은 F.K의 위치를 기준으로 정해야 한다.**
  - 그러고 나중에 객체 관점에서 볼 때 그 반대편(@OneToMany가 붙은 쪽)에 무언가를 넣고 싶다면 연관관계 편의 메소드를 활용하면 된다.
  









