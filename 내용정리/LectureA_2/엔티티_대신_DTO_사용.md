```java
@GetMapping("/api/v1/members")
public List<Member> membersV1() {
    List<Member> members = memberService.findMembers();
    return members;
}
```
Entity 자체를 반환하는데 사용하였다.
postman을 통해 v1의 url을 GET요청 해보았다.

```json
[
    {
        "id": 1,
        "name": "maddison",
        "address": {
            "city": "london",
            "street": "london-street",
            "zipcode": "177-999"
        },
        "orders": []
    },
    {
        "id": 2,
        "name": "kane",
        "address": {
            "city": "seoul",
            "street": "g-26",
            "zipcode": "144232"
        },
        "orders": []
    }
]
```
위와 같은 결과가 나왔다.

- 엔티티를 반환에 사용하면 엔티티의 모든 정보가 외부에 노출되기 때문에, 회원의 이름 정보만 원하는 경우 불필요한 정보까지 포함된다.
- 이를 해결하기 위해 1차적으로 @JsonIgnore 어노테이션을 사용할 수도 있다.
  - 만약 orders 정보를 노출시키고 싶지 않다면,
  - ```java
    public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    
    }
    ```
  - 하지만, @JsonIgnore를 사용 하였을 시에 다양한 상황의 API 스펙을 모두 만족 시킬 수는 없다.
  - 조회 API가 하나가 아닐 뿐더러 많은 클라이언트에서 다양한 스타일의 API를 요구할텐데, 어떤 클라이언트에서는 orders가 필요하고 누구는 안필요하고,,,
  그때마다 @JsonIgnore을 사용하여 Entity에 이러한 것들을 포함시키기 시작하면 답이 안나오는 상황이 발생한다.
  - 그리고 이렇게 되면 Entity에 화면을 뿌리기 위한 로직이 추가되었기 때문에(Entity에 프레젠테이션 계층을 위한 로직이 추가됨), 의존관계 Entity로 들어와야 하지만
  Entity에서 의존관계가 나가버린다. 이렇게 되면 양방향 의존관계가 걸리면서 애플리케이션을 수정하기 어렵다.
  - **추가로 Member Entity의 name필드가 만약 username 필드로 변경 된다면 API 스펙이 바뀌어 버리는 심각한 문제가 발생한다.**
    - ```json
      [
         {
           "id": 1,
           "username": "maddison",
           "address": {...}
      ...
      ```
    - 현재 List<Member>가 반환형이기 때문에 GET요청을 했을때 결과는 [] 배열 형태이다. 이 결과에서 예를들어 count라는 변수를 추가하고 싶다면, 이 상태에서는 하지 못하게 된다.
    Member객체만 들어갈 수 있는 배열이기 때문이다. 하지만 아래와 같이 결과가 나온다면 상관없다.
      - ```json
        {
          "count":4,
          "data": [
                    {
                        "id": 1,
                        "name": "maddison",
                        "address": {
                            "city": "london",
                            "street": "london-street",
                            "zipcode": "177-999"
                        },
                        "orders": []
                    },
                    {
                        "id": 2,
                        "name": "kane",
                        "address": {
                        "city": "seoul",
                        "street": "g-26",
                        "zipcode": "144232"
                        },
                        "orders": []
                    }
                  ] 
        }
        ```
      - 이런 형태라면 스펙을 확장할 수 있다는 것이다.
      - 이전에 배열로 직접 반환하는 것보다 유연성이 높아졌다.


```java
@GetMapping("/api/v2/members")
public Result membersV2() {
    List<Member> findMembers = memberService.findMembers();

    List<MemberDto> collect = new ArrayList<>();
    for (Member findMember : findMembers) {
        MemberDto memberDto = new MemberDto(findMember.getName());
        collect.add(memberDto);
    }

//        List<MemberDto> collect = findMembers.stream()
//                .map(m -> new MemberDto(m.getName()))
//                .collect(Collectors.toList());
                
    Result result = new Result(collect);
    return result;
}
```
v1처럼 반환 타입으로 List를 바로 반환하게 되면 JSON 배열 타입으로 나가기 때문에, Result 객체로 List를 감싸서 반환하였다. 
이렇게 되면 name을 username을 바꾸어도 API 스펙이 바뀌지는 않는다.

Entity를 DTO로 변환하는 과정이 귀찮기는 하지만 Entity가 변경이 되도 API 스펙 자체가 변하지는 않는 장점이 있고,
한번 감싸서 반환 했기 때문에 유연성이 생긴다.

```java
@Data
@AllArgsConstructor
static class MemberDto {
    private int count; //count필드 추가
    private String name;
}

...
@GetMapping("/api/v2/members")
public Result membersV2() {

...

Result result = new Result(collect.size(),collect); 
return result;

```

[요청결과]
```json
{
  "count": 2,
  "data": [
            {...},
            {...}
          ] 
}
```




