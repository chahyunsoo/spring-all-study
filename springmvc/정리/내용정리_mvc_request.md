### 요청 매핑

#### @RequestMapping
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

#### @GetMapping

위의 코드를 @GetMapping으로 바꿀 수도 있다. 
```java
@GetMapping(value = "/hello-request)
...
```
이 상태에서도 GET 요청을 제외한 요청을 보내게 되면
>WARN 5769 --- [nio-8080-exec-4] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' is not supported]

POST 요청 메소드를 지원할 수 없다고 뜬다.

#### @PathVariable(경로 변수)

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

### 요청 파라미터 

#### 쿼리 파라미터, HTML Form
HttpServletRequest의 request.getParameter()를 사용하면 두가지 요청 파라미터를 조회할 수 있었다.

- GET - 쿼리 파라미터 전송
  - ex. http://localhost:8080/request-param?username=soo&age=20
- POST - HTML Form 전송
  - POST / request-param...
  - Content-Type : application/x-www-form-urlencoded
  - username=soo&age=20
- GET 쿼리 파리미터 전송 방식이든, POST HTML Form 전송 방식이든 둘다 형식이 같으므로 구분없이 조회할 수 있다.
  이것을 요청 파라미터(request parameter) 조회라 한다.

```java
@Slf4j
@Controller
public class RequestParamController {
    @RequestMapping("/request-param-v1")
    public void requestParam1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={},age={}",username,age);

        response.getWriter().write("ok");

    }
}
```

http://localhost:8080/request-param-v1?username=hello&age=20 을 입력하여 GET 요청을 보내면,
log에 username=hello , age=20 결과가 찍힌다. 이미 앞에서 servlet 공부할때 했던 내용들인 것 같다.

#### @RequestParam

- @RequestParam은 파라미터 이름으로 바인딩을 한다.
- @ResponseBody를 사용하면 반환값이 뷰 리졸버를 거쳐서 뷰를 찾는 것이 아니라, HTTP 메시지 Body에 
직접 값을 집어넣는다.

```java
@ResponseBody //이걸 붙히면 ok라는 문자가 HTTP 응답 메시지에 바로 딱 넣어서 반환한다. @RestController와 같은 역할
@RequestMapping("/request-param-v2")
public String requestParamV2(@RequestParam("username") String myName, @RequestParam("age") int myAge) {
    log.info("username={},age={}", myName, myAge);
    return "ok";
    //이렇게 되면 위에 @Controller 어노테이션에 반환값이 String인 메소드여서 뷰 리졸버가 ok라는 뷰를 찾게 된다. 그래서 ok라는 문자를 HTTP 메시지에 바로 넣고 싶으면
    //@Controller -> @RestController로 바꿔도 된다.
    // 하지만 지금 취지는 그것이 아니기 때문에, 지금은 ok라는 문자를 HTTP 메시지에 바로 넣고 싶다. 그러면 @ResponseBody 어노테이션을 한번 추가해보자.
}
```

@RequestParam의 속성에 required 속성을 이용하여 파라미터에 값이 반드시 들어와야 하는지를 정할 수 있다.
기본값은 required=true 이다. 아무것도 적지 않으면 true라는 소리이다. 

localhost:8080/request-param-required?username=my&age=20 url이 있으면,
```java
@ResponseBody
@RequestMapping("/request-param-required")
public String requestParamRequired(@RequestParam(required = true) String username, @RequestParam(required = false) int age){
    log.info("username={},age={}",username,age);
    return"ok";
}
```
username은 required=true(기본값)이기 때문에 username이 반드시 들어와야 한다.
age는 required=false 로 설정하였기 때문에 age는 반드시 들어올 필요는 없다.

하지만 한가지 주의할 점이 있다.
```java
(@RequestParam(required = true) String username, @RequestParam(required = false) int age)
```
위의 인자가 포함된 코드를 postman을 이용해 요청을 보내보았더니 이런 결과가 떴다.
```json
{
"timestamp": "2023-08-30T13:32:24.890+00:00",
"status": 500,
"error": "Internal Server Error",
"path": "/request-param-required"
}
```
왜 500에러가 떴을까????
위의 인자를 한번 보면 두번째 인자에 int age가 들어가 있다. required=false 였기 때문에 값이 안 들어가도 된다.
값이 들어가지 않으면 null이 들어가는 셈인데, int는 기본형이기 때문에 null이 들어갈 수 없다. 무조건 0이라도 초기화가 되야 한다.
그래서 두번째 인자를 `Integer age` 로 바꾸어 객체 형태로 만들어야지 null값이 들어갈 수 있다.

Integer age로 바꾼 후 다시 http://localhost:8080/request-param-required?username=hello 요청을 보내 보니, 
콘솔에 `username=hello,age=null` 이 찍히는 것을 확인할 수 있었다.


추가로 위의 상황에서 http://localhost:8080/request-param-required?username= 이렇게 요청을 하면
null이 아니라 ""(빈 문자) 로 통과를 해서 `username= ` 이 찍힌다.
아래 코드는 defalutValue 설정 값을 주어서 파라미터의 값이 들어오지 않을시 defalutValue 설정 값이 들어오게 한다.

```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault (@RequestParam(required = true,defaultValue = "guest") String username,
                                   @RequestParam(required = false,defaultValue = "-1") int age) {
        //username 값이 없으면 guest가 값이 된다는 뜻.
        log.info("username={},age={}", username, age);
        return "ok";
}
```
아까는 @RequestParam의 설정들 중에 required=false 일 때, null로 인하여 int를 Integer로 변환 하였지만,
defaultValue의 값이 -1로 설정했기 때문에, null로 값이 들어올리가 없다.그래서 다시 int로 변환해주어도 괜찮다.
그런데 사실 defaultValue를 사용하면 required=true가 있을 필요가 없다. 무조건 값이 defaultValue 값으로 
들어가기 때문이다.

마지막으로 파라미터를 Map으로 조회할 수 있다.

#### ModelAttribute

```java@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttribute_V1(@RequestParam String username,@RequestParam int age) {
    HelloData helloData = new HelloData();
    helloData.setUsername(username);
    helloData.setAge(age);

    log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
    log.info("helloData={}", helloData);
    return "ok";
}
```
위의 코드를 @ModelAttribute 어노테이션을 이용하여 바꿀 수 있다. (HelloData객체는 username과 age필드만 가지고 있는 DTO라고 가정)

```java
@RequestParam String username;
@RequestParam int age;

HelloData data = new HelloData();
data.setUsername(username);
data.setAge(age);
```
스프링인 위의 코드를 @ModelAttribute 로 대체 해준다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttribute_V2(@ModelAttribute HelloData helloData) {
    log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
    log.info("helloData={}", helloData);
    return "ok";
}
```

우선 @ModelAttribute는 이 순서대로 동작한다.(위의 코드를 예시로)
- HelloData 객체를 생성한다.
- 요청 파라미터 이름으로 HelloData 객체의 프로퍼티(setXxx,getXxx)를 찾는다. ?username=my 이면 setUsername()을 찾고
  파라미터의 값을 입력한다.


이 @ModelAttribute 어노테이션을 생략해도 된다. 하지만 좀 전에 @RequestParam도 생략가능하다고 했는데,
조금 헷갈리긴 한 것 같다.
그래서 스프링은 int,String,Integer 들과 같은 단순 타입은 => @RequestParam을 지원하고,
그 외의 타입은 @ModelAttribute를 지원하지만, argument resolver 타입으로 지정(ex. HttpServletRequest)한 것은 제외한다.


### 요청 Message

#### 단순 text








  








