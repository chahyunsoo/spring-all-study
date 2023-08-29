#### @RestController

- @RestController
  - @Controller는 반환 값이 String 이면 뷰 이름으로 인식된다. 그렇기 때문에 뷰를 찾고 뷰가 랜더링 된다. 
  - @RestController 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 Body에 바로 입력한다.
    따라서 실행 결과로 ok 메세지를 받을 수 있다. 

#### log 레벨 설정

- LEVEL: TRACE > DEBUG > INFO > WARN > ERROR 
- 개발 서버는 debug 출력
- 운영 서버는 info 출력

```java
private final Logger log = LoggerFactory.getLogger(getClass());
```
이거 대신에 @Slf4j 어노테이션를 쓸 수 있다.


- 올바른 로그 사용법
  - log.debug("data="+data)
  로그 출력 레벨을 info로 설정해도 해당 코드에 있는 "data="+data가 실제 실행이 되어 버린다.
  즉 연산이 실행되어 불필요한 연산이나 쓸모없는 리소스를 사용한다.
  - log.debug("data={}", data)
  로그 출력 레벨을 info로 설정하면 아무일도 발생하지 않는다. 단순하게 파라미터만 넘기는 것이지
  연산을 하지 않기에 위와 같은 불필요한 연산이 발생 하지 않는다.