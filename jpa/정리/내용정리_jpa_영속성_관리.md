### 1차 캐시에서 조회 vs DB에서 조회

우선 Member객체가 1차 캐시에 있다고 가정하자. 1차 캐시에는 'key'가 DB와 매핑한 P.K가 되고, 'value'로는 Entity객체(Member)가 온다.
```java
//엔티티를 생성만하고 영속하지는 않은 비영속 상태
Member member1=new Member();
member1.setId("member1");
member1.setName("memberName1");

//영속 상태
em.persist(member1);
```
이때 내가 방금 영속 상태로 만든 member1 객체를 조회하고 싶다면, jpa는 바로 DB에서 조회하는게 아니라 1차 캐시에서 조회를 시도한다.
em.find(Member.class,member1); 로 member1객체를 찾게 되면, member1은 바로 1차 캐시에서 조회가 가능하다. 왜냐하면 내가 member1객체를 영속 상태로 만들어버렸기 때문이다.

만약 여기서 1차 캐시에 존재하지 않은(내가 영속 상태로 만들지 않은) 객체 member2를 조회하고 싶어졌다.
하지만 member2는 1차 캐시에 존재하지 않기 때문에 위에서 member1처럼 바로 1차 캐시에서 조회하는 것이 불가능하다.
그래서 우선 em.find(Member.class,member2); 를 하게 되면 DB에서 조회를 하게 된다(DB에 member2가 있다는 가정하에).
결론적으로 member2를 조회 하게 되면, 이 DB에서 조회한 member2는 1차적으로 1차 캐시에 저장되고 그 다음 이제서야 member2를 반환한다.
이후에 member2를 다시 조회 한다면, 이때는 1차 캐시에 있는 member2가 조회된다.
 
- 참고
  - EntityManager는 DB Transaction 단위로 만들고 DB Transaction이 끝나면 바로 종료가 된다. 즉, 고객의 요청이 하나 들어와서 비즈니스 로직이 끝나 버리면
  영속성 컨텍스트를 지우게 되다는 것이다. 1차 캐시도 다 날라간다. 그래서 이 짧은 시간에서만 1차캐시에서 조회하는 이 매커니즘이 이득이 있기 때문에, 여러 명의 고객이 사용하는
  캐시와는 거리가 멀다. 애플리케이션 전체에서 공유하는 캐시는 2차 캐시라고 한다. 1차 캐시에는 Transaction안에서만 효과가 있기 때문에 성능 이점을 얻을 수 있는 장점이 많지는 않다.
  
  - em.persist(member);를 하고 이 1차 캐시에 존재하는 member를 다시 find(조회)할 때 DB에 select쿼리가 나가지 않는다.(이건 뭐 당연한 사실)

### 영속 엔티티의 동일성 보장

```java
Member findmember1 = entityManager.find(Member.class, 101L);
Member findmember2 = entityManager.find(Member.class, 101L);
System.out.println(findmember1 == findmember2);  //TRUE
```
같은 Transaction안에서 위의 코드 처럼 같은 것을 조회했을때 == 비교를 하면 True가 나옴.

### 엔티티를 등록할때 Transaction을 지원하는 쓰기 지연

```java
EntityManager em = emf.createEntityManager();
EntityTransaction transaction = em.getTransaction();
//엔티티 매니저는 데이터 변경시 Transaction을 시작해야 한다.

transaction.begin(); // Transaction 시작

em.persist(memberA);
em.persist(memberB);
//여기까지 insert 쿼리문을 DB에 보내지 않는다.

//커밋하는 순간 데이터베이스에 insert 쿼리를 보냄.
transaction.commit(); // Transaction 커밋
```

- 배치 사이즈를 10으로 설정하면, 이 사이즈 만큼 모아서 DB에 한번에 보내고 commit을 한다.(버퍼링같은 개념)
  `<property name="hibernate.jdbc.batch_size" value="10"/>`

### 엔티티를 수정할때 변경 감지

```java
//p.k 200에 해당하는 Entity는 현재 memberA라고 가정.
Member member = entityManager.find(Member.class, 200L);
member.setName("changedmemberA");

//entityManager.persist(member);
//다시 persist할 필요가 없음
```

jpa를 자바 컬렉션 처럼 생각하면 편하다. 내가 자바 컬렉션을 사용할 때 특정 값을 변경하게 되면 그 변경한 값을
다시 컬렉션에 저장하는 과정을 거치지 않는다.
jpa에서도 마찬가지... 이미 내가 persist했던 member객체인데 그 member객체의 이름 필드를 변경했다고 해서 이 변경한 값의 객체인 member를 
다시 persist할 필요가 있을까? 느낌상 값을 일단 변경했으니까 이 변경한 내용을 DB에 반영하기 위해서는 update 쿼리를 날려야 할 것 같은데,,,,,?

--> 그럴 필요가 없이 DB에 값이 변경이 된다. 왜?????????? 
 - commit을 하면 내부적으로 flush가 호출이 된다. commit을 하게 되면 내부적으로 엔티티와 스냅샷을 비교하게 된다.
1차 캐시에 안에는 p.k , p.k에 해당하는 Entity , 그리고 스냅샷이 있다. 스냅샷은 내가 값을 읽어온 최초 시점에 상태를 스냅샷으로 임시 저장을 한다.
이 상태에서 내가 값을 변경한다면, jpa가 Transaction이 commit되는 시점에 내부적으로 flush가 호출이 되면서 1차 캐시에 있는 것들( p.k , p.k에 해당하는 Entity , 그리고 스냅샷)
을 **비교**한다. 비교를 한 후 Entity가 변경이 됐으면(위의 코드에서는 name필드가 memberA -> changedmemberA 로 변경)
update 쿼리를 쓰기 지연 저장소에 만들어 둔다. 그리고 이 update 쿼리를 DB에 반영을 하고 commit을 하게 된다. 이것을 **변경 감지(Dirty Checking)**라고 함.

### 엔티티 삭제

엔티티 삭제도 위에서 본 변경 감지의 매커니즘과 비슷하게 이번엔 delete 쿼리가 똑같이 동작한다.

### 플러시 - 영속성 컨텍스트의 변경 내용을 DB에 반영함

플러시가 발생하면 1. 변경 감지(Dirty Checking) , 2. 수정된 엔티티를 쓰기 지연 SQL 저장소에 등록 , 3. 쓰기 지연 SQL 저장소의 쿼리를 DB에 전송(등록,수정,삭제 쿼리)
<br>이 3가지의 과정을 거치게 된다.

플러시가 발생한다고 해서 DB의 Transaction이 commit되는 것은 아니다. 그러면 영속성 컨텍스트를 어떻게 플러시할까?
- em.flush()를 직접 호출하여 플러시 한다.
- Transaction을 commit하여 플러시를 자동적으로 호출하게 한다.
- JPQL 쿼리를 실행하여 플러시를 자동적으로 호출하게 한다.
  - ```java
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    
    //JPQL 쿼리 실행
    query= em.createQuery("select m from Member m" , Member.class);
    List<Memeber> members = query.getResultList;
    ``` 
    원래 알던 내용을 토대로 생각했을때, em.persist()를 호출하면 아직 DB에 insert 쿼리가 날라가기 전이다. 하지만 아래에 JPQL을 통해 select 쿼리를 호출하게 되면 아직 DB에 저장된 것이 없으니 불러올 값이 없을 것이다.
    이 부분에서 문제가 생길 수 있기 때문에, jpa는 **JPQL 쿼리를 실행했을시 자동으로 플러시를 발생**시키고 그 다음 DB에 쿼리가 날라가게 해두었다. 그래서 member 1 2 3이 조회가 된다.

- 결론적으로 플러시는 영속성 컨텍스트를 비우지 않는다
- **영속성 컨텍스트의 변경 내용을 DB에 동기화하는게 플러시다.**
- Transaction이라는 작업 단위가 여기서 키 포인트이다. **commit 직전에만 동기화**를 하면 된다.

### 준영속 상태
 
간략하게 정리하자면, em.persist(member)를 하면 이것은 영속 상태가 되는 것을 알고 있다. 
그리고 맨 위에서 어떤 객체를 조회 하려 할 때 그 객체가 1차 캐시에 없다면, 나는 DB에서 조회를 하고 그것을 1차 캐시에 올린 후 반환을 하는 구조로 돌아가는 것을 알고 있다.
여기서 1차 캐시에 올라간 상태를 영속 상태(jpa가 관리하는 상태)라고 했다.
<br>-> em.persist()를 하여도 영속 상태가 되지만, em.find()를 했을 때 1차 캐시에 없다면, DB에서 조회하여 1차 캐시에 저장하는 것도 영속 상태인 것이다.

그렇다면 본론을 돌아와 준영속 상태는 무엇일까???
<br>준영속 상태란 영속성 컨텍스트에 의해 관리된 영속 상태인 Entity가 영속성 컨텍스트에서 분리(detached)된 상태를 말한다.
준영속 상태가 되면 영속성 컨텍스트가 제공하는 기능(변경 감지...등등)들을 사용하지 못하게 된다.
- 준영속 상태로 만드는 방법들
  - em.detach(entity);
    - 인자로 전달된 특정 Entity만 준영속 상태로 만든다.
  - em.clear();
    - 영속성 컨텍스트를 완전하게 초기화 한다.
  - em.close();
    - 영속성 컨텍스트를 종료한다.

```java
Member member = entityManager.find(Member.class, 200L);
System.out.println("--------------");
member.setName("rerechangedmemberAA");

entityManager.detach(member);

//commit하는 시점에 DB에 insert 쿼리가 날라가게 된다.
transaction.commit(); //위에서 저장하고 commit을 한다.

[실행결과]
Hibernate:
        select
            member0_.id as id1_0_0_,
            member0_.name as name2_0_0_
        from
            Member member0_
        where
            member0_.id=?
--------------

```
Entity를 준영속 상태로 만들게 되면 Entity를 변경을 해도 변경 감지가 일어나지 않아 update 쿼리가 날라가지 않는다.


```java
Member member = entityManager.find(Member.class, 200L);
System.out.println("--------------");
member.setName("rerechangedmemberAA");

entityManager.clear();


//영속성 컨테스트를 초기화하고 다시 조회한다면?
Member member2 = entityManager.find(Member.class, 200L);
member2.setName("newMemberAA");
//이러면 update 쿼리가 날라간다.
        
transaction.commit();

[실행결과]
        Hibernate:
            select
                member0_.id as id1_0_0_,
                member0_.name as name2_0_0_
            from
                Member member0_
            where
                member0_.id=?
--------------
        Hibernate:
            select
                member0_.id as id1_0_0_,
                member0_.name as name2_0_0_
            from
                Member member0_
            where
            member0_.id=?
        Hibernate: 
    /* update
        jpa.Member */ update
            Member
        set
            name=?
        where
            id=?
```

### 정리

[1] jpa에서 가장 중요하게 생각해야할 것
- 객체 <-> DB를 어떻게 잘 Mapping 할지
- 영속성 컨텍스트
  - 뜻 : Entity를 영구적으로 저장하는 환경
  - EntityManager를 통해서 영속성 컨텍스트에 접근함
  - 일단 지금은 EntityManager를 하나 생성하게 되면 그 안에 영속성 컨텍스트가 있다고 생각하면 될 듯 하다.
  - 일단 지금은 1차 캐시가 영속성 컨텍스트라고 생각하면 편할 것 같다.

[2] Entity 생명주기 4가지도 공부했다.
- 영속  : persist를 통해 집어 넣은 상태, find를 통해 가져온 상태
- 비영속 : 아무것도 아닌 상태
- 준영속 : 영속성 컨텍스트에서 분리된 상태
- 삭제  : DB에 delete 쿼리를 날리고 싶을때

[3] 영속성 컨텍스트의 이점
- 1차 캐시 : 동일한 Transaction안에서 한 번 조회했던 것 또 조회하면 DB에 쿼리가 날라가지 않는다. 그런데 사실 고객이 10명이 동시에 접근하면, 10명 모두 별도의 1차 캐시를 가지기 때문에 성능의 이점이 드라마틱하지는 않다.
- 동일성 보장 : 1차 캐시에서 가져온 것을 사용하기 때문에 == 비교를 하게 되면 true 이다.
- Transaction을 지원하는 쓰기 지연 : 버퍼링 같은 개념, 쓸 것들 모았다가 한번에 반영하는,,,
- 변경 감지(Dirty Checking) : update를 할 때 변경된 것을 jpa가 자동으로 인식해서 알려주는 것      
- 지연 로딩(Lazy Loading)

[4] 플러시
- Transaction을 커밋하거나 쿼리를 실행할 때 플러시는 기본값을 설정되어 있다.(직접 플러시 할 수도 있다.)
- 플러시는 영속성 컨텍스트를 비우지 않는다.
- 영속성 컨텍스트의 변경 내용을 DB에 동기화하는 것이다.
- commimt 직전에만 동기화를 하면 된다.
- Transaction이라는 개념이 정말 중요한 것 같다.
- 영속성 컨텍스트랑 Transaction의 주기를 맞춰서 설계를 해야 문제가 발생하지 않는다.

[5] 준영속 상태
- 영속성 컨텍스트의 Entity가 영속성 컨텍스트에서 분리된 상태
- 영속성 컨텍스트가 제공하는 기능들을 사용하지 못한다.
- 준영속 상태로 만드는 방법
  - em.detach(entity) -> 인자로 전달된 Entity만 준영속 상태로 전환한다.
  - em.clear()        -> 영속성 컨텍스트를 완전히 초기화 한다. 
  - em.close()        -> 영속성 컨텍스트를 종료한다. 
  
 

  












