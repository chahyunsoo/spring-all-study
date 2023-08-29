### 요청 매핑

#### RequestMapping
- HTTP 메서드 
  - @RequestMapping 에 method 속성으로 HTTP 메서드를 지정하지 않으면(ex. method=RequestMethod.GET ) HTTP 메서드와 무관하게 호출된다.
    모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE


하지만 인자에 method설정을 해주면 이젠 다르다. 아래 코드처럼 RequestMethod.GET을 추가하면
GET요청만 정상적으로 받아들이고 그 외의 요청을 하게 되면 405 상태코드가 뜬다.
```java
@RequestMapping(value = "/hello-request", method = RequestMethod.GET)
public String helloRequest() {
    log.info("add method = RequestMethod.GET");
    return "ok";
}
```

#### GetMapping

위의 코드를 @GetMapping으로 바꿀 수도 있다. 
```java
@GetMapping(value = "/hello-request)
...
```
이 상태에서도 GET 요청을 제외한 요청을 보내게 되면
>WARN 5769 --- [nio-8080-exec-4] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' is not supported]

POST 요청 메소드를 지원할 수 없다고 뜬다.

#### PathVariable(경로 변수)

```java
@GetMapping("/mapping/{userId}")
public String mappingPath(@PathVariable("userId") int dataId) {
    log.info("mapping Path userId={}", dataId);
    return "ok";
}
```
{userId}를 PathVariable로 지정하여 url을 localhost:8080/mapping/1 이라고 보내면,
log.info()를 통한 로그가 userId=1 이라고 찍히게 된다.
이렇게 url 경로에 어떤 값을 템플릿 형식으로 쓸 수 있고, @PathVariable로 꺼내어 사용할 수 있다는 것이다.

@PathVariable이 여러개 일때도 가능하다.
```java
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath_v2(@PathVariable("userId") int dataId, @PathVariable("orderId") int dataOrderId) {
    log.info("mapping Path userId={},orderId={}", dataId,dataOrderId);
    return "ok";
}
```


#### RequestMapping - API





