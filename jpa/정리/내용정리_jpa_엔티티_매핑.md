### 엔티티 매핑

- 객체와 테이블 매핑
- 필드와 컬럼 매핑
- P.K 매핑
- 연관관계 매핑

#### 객체와 테이블 매핑

- @Entity
  - @Entity가 붙은 클래스는 JPA가 관리하는 엔티티
  - 기본 생성자가 필수이다 -> 인자가 존재 하지 않는 public 또는 protected 생성자
  - 저장할 필드에는 final 붙이면 안됨
  - final 클래스, inner 클래스, interface, enum을 사용할 수 없다.

- @Table
  - name 속성으로 매핑할 테이블의 이름을 지정할 수 있다.

#### 데이터 베이스 스키마 자동 생성

- 속성, hibernate.hbm2ddl.auto(Maven 기준)
  - <property name="hibernate.hbm2ddl.auto" value="create" /> 
  - create : 기존 테이블 삭제 후 다시 생성(DROP + CREATE)
  - create-drop : create와 동일하지만 종료 시점에 테이블을 DROP
  - update : 변경 부분만 반영됨(대신 운영 DB에는 사용하면 안된다), 지우는 건 안됨
  - validate : 엔티티와 테이블이 정상적으로 매핑되었는지만 확인함
  - none : 아무것도 사용하지 않음
  - 주의 할 점 =>
    - 운영에서는 절대로 create, create-drop,update를 사용하면 안된다.
    - 개발 초기 단계때는 create or update
    - Test 서버는 update or validate
    - 스테이징과 운영 서버는 validate or none

- DDL 생성 기능
  - @Column(unique = true, length = 10) private String name;
    - 이 조건은 이름 컬럼은 반드시 값이 들어가야 하고 길이는 10이하로 제한하였다.
    - 위의 unique 조건은 JPA의 실행에는 영향을 미치지 않고, 단순히 DB에 날라가는 alter table 쿼리문이
    하나 실행되는 것 뿐이다.
    -  다시 말해 DDL 생성이 켜져있을 때, 처음 애플리케이션 실행 시에만 DDL에서 작동할 뿐, 
    JPA의 기능을 활용하는 런타임에서는 사용하지 않는다는 내용.

#### 필드와 컬럼 매핑

- @Column
  - 컬럼 매핑
    - 속성
      - name : 필드와 매핑할 컬럼 이름
      - insertable,updatable : 등록, 변경 가능 여부 (default값은 true)
      - nullabe(DDL) : null값을 허용하냐 안하냐 여부 파악, false이면 DDL 생성할때 not null 제약 조건을 붙힌다.
      - unique(DDL) : 하나의 컬럼에 간단히 UNIQUE 제약 조건을 걸때 사용, 하지만 컬럼위에 unique 속성을 넣으면 DDL 생성시 alter table 이름이 랜덤 값처럼 생성되어서
      한번에 파악하기 힘들다. 그래서 @Table 어노테이션에 uniqueConstraints 속성을 이용하여 랜덤이 아닌 이름까지 직접 지정해줄 수 있다.
      - columnDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 넣어줄 수 있다.
        ```java
        @Column(columDefinition="varchar(30) default "EMPTY")
        private String name;
        [DDL 결과]
        create table Member {
        ...
        name varchar(30) default "EMPTY"
        }
        ```
      - length : String 타입일때만 사용 가능하며, 길이의 제약 조건을 정하는 
      - precision, scale(DDL) : 아주 큰 숫자나 소수점 자리까지 이용할때 쓰면 됨, BigDecimal 타입에서 사용한다.
- @Temporal
  - 날짜 타입 매핑
  - DATE(날짜), TIME(시간), TIMESTMAP(날짜+시간) 3가지 타입이 존재
  - 하지만 최근에는 변수 타입을 LocalDate,LocalDateTime을 사용하기 때문에 @Temporal 어노테이션을 잘 사용하지는 않는다.
- @Enumerated
  - enum 타입 매핑, EnumType.ORDINAL을 사용 X
  - EnumType.STRING : enum의 이름을 DB에 저장,DB에 값이 들어갈때 String타입으로 들어감
    - ```text
      ID  AGE  CREATEDDATE  DESCRIPTION  LASTMODIFIEDDATE  ROLETYPE  NAME  
      1   null   null          null	        null	       USER   memberA
      ```
  - EnumType.ORDINAL : enum의 순서를 DB에 저장, DB에 값이 들어갈때 integer타입으로 들어감
    - ```text
      ID  AGE  CREATEDDATE  DESCRIPTION  LASTMODIFIEDDATE  ROLETYPE  NAME  
      1   null   null          null	        null	         0    memberA
      ```
    - **하지만** 기본값 EnumType.ORDINAL을 사용하면 enum 클래스에 새로운 값이 생겨 그 값이 제일 앞에서 정의 된다면,
    DB에서는 그 값을 0번으로 인식하여 기존의 0번과 충돌하는 현상이 일어난다. 그래서 EnumType의 ORDINAL을 사용하면 위험하다.
- @Lob
  - LOB은 가변길의를 갖는 큰 데이터를 저장하는데 사용하는 데이터형이다.
  - BLOB(binary 데이터(byte[])를 저장하는데 사용), CLOB(문자기반(char[],String)을 데이터를 저장하는데 사용) 매핑
  - 특별한 속성은 존재하지 않는다.
- @Transient
  - 특정 필드를 컬럼에 매핑하고 싶지 않을때 사용


#### P.K 매핑

- @Id
  - 내가 직접 P.K값을 할당하고 싶을 때
- @GeneratedValue
  - 값을 자동 생성
  - strategy
    - AUTO : DB 방언에 맞게 자동 생성
    - IDENTITY : P.K생성을 DB에 위임한다.
      - DB에 값을 넣기 전까지 P.K값을 모른다.
      - em.persist() 시점에 즉시 INSERT문을 실행 하고 DB에서 식별자를 조회할 수 있다.
      - SEQUENCE 전략이나 직접 값을 세팅하면 값을 알고 있기 때문에 commit을 하는 시점에 쿼리가 날라간다는 차이가 있다. 이미 값을 알고 있기 때문이다.
    - SEQUENCE : sequence object를 통해서 값을 가져온 다음에 세팅을 한다.
    - TABLE : 키 생성용 테이블 사용, 모든 DB에서 사용하고 , @TableGenerator 필요
      - @TableGenerator 속성에는 name,table,initialValue,allocationSize,pkColumnValue... 등이 있다.

- 권장하는 식별자 전략은?
  - P.K 제약 조건 : null이면 안되고 유일해야 하며, 변하면 안된다.
  - 하지만 먼 미래까지 이러한 조건을 만족시키는 자연키는 찾기 힘들다. 대신 대체키를 사용하자
  - 주민등록번호를 P.K로 사용하기는 적절치 않다.
  - 그래서 Long형  + 대체 키 + 키 생성 전략을 이용하자




 

