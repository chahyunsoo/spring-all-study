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
- @ResponseBody를 사용하면 반환값이 뷰 리졸버를 거쳐서 뷰를 찾는 것이 아니라, HTTP 메시지 Body에 직접 값을 집어넣는다.

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

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttribute_V1(@RequestParam String username, @RequestParam int age) {
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

앞에서 봤던 요청 파라미터의 내용(GET-쿼리 파라미터, POST-Form)들과는 다르게, Http Message Body에 직접 데이터가 담겨서
넘어오는 경우에는 @RequestParam, @ModelAttribute를 사용할 수 없다. 

```java
@PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody={}", messageBody);
        response.getWriter().write("ok");
}

@PostMapping("/request-body-string-v2")
public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("messagebody={}", messageBody);
    responseWriter.write("ok");
}
```
- InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회 
- OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

POST요청으로 localhost:8080/request-body-string-v1(또는 v2) 를 요청하고 요청 메시지에 text타입으로 문자열을 입력하면
message = 문자열이 찍히게 된다. 하지만 위의 코드를 보면 현재 나는 servlet에 대한 코드가 필요가 없다. HttpServletRequest가 통으로 필요한 것이 아니기 때문에,
InputStream으로 바꿔서 사용할 수 있다.
위의 코드는 InputStream인자를 이용하여 바로 메시지를 Body내용을 조회한 것이다.

하지만 여전히 뭔가 불편한 감이 있는 것 같다. 뭔가 막 stream을 바꾸고(ex.String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);)
이런 과정들이 복잡한 것 같다.

이 모든 복잡한 과정들을 HttpEntity의 이용과 Http message converter가 동작을 함으로써 해결할 수 있다.
```java
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
    String messagebody = httpEntity.getBody(); //Http 메시지에 있는 Body를 꺼낸다. 변환된 Body를 꺼낼 수 있다.
    log.info("messagebody={}", messagebody);
    return new HttpEntity<>("ok");
}
```
코드가 확연히 줄었다. 이 코드는 이렇게 해석할 수 있다.
메소드의 인자로 HttpEntity<String>을 넣어줌으로 인하여 스프링이 알아서 <String> 이니까 Http Body에 있는 것을 문자로 바꿔서 넣어준다.
그래서 `String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);` 
이 코드가 생략되며 스프링이 이 코드를 대신 실행해주면서 Http message converter가 동작을 한다.
그리고 Http 메시지에 있는 Body를 꺼내고 메소드의 반환타입을 HttpEntity<String>으로 지정하면서,
기존의 코드와 동일하게 반환할 수 있다. 그래서 마치 Http 메시지를 그대로 주고 받는 형식으로 만들 수 있다.

- HttpEntity 정리
  - HttpEntity: HTTP header, body 정보를 편리하게 조회
    - 메시지 바디 정보를 직접 조회
    - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X 
    
  - HttpEntity는 응답에도 사용 가능
    - 메시지 바디 정보 직접 반환 
    - 헤더 정보 포함 가능
    - view 조회X
    - HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다. 
  
- RequestEntity
  - HttpMethod, url 정보가 추가, 요청에서 사용 
  
- ResponseEntity
  - HTTP 상태 코드를 넣을 수 있다, 응답에서 사용
  - return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)

```java
@PostMapping("/request-body-string-v4")
public ResponseEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity) throws IOException {
    String messagebody = httpEntity.getBody();
    HttpHeaders headers = httpEntity.getHeaders();

    log.info("messagebody={}", messagebody);
    log.info("headers={}", headers);
    return new ResponseEntity<>("ok", HttpStatus.OK);
}
```

그런데 여기서 또 드는 생각이 HttpEntity를 써야돼?라는 생각이 들 수 있다.(그렇다고 치자.)
그래서 스프링은 @RequestBody 어노테이션을 지원한다.

```java
@ResponseBody
@PostMapping("/request-body-string-v5")
public String requestBodyStringV5(@RequestBody String messagebody) throws IOException {
    log.info("messagebody={}", messagebody);
    return "ok";
}
```

그래서 @RequestBody를 인자로 사용하면 Http message Body의 내용을 편리하게 읽을 수 있다.

- @RequestBody
  - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
  - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 
- @ResponseBody
  - 메시지 바디 정보 직접 반환(view 조회X)
  - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용

- 참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다. 
이렇게 메시지 Body를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와는 전혀 관계가 없다.

> 그래서 결론은 요청 파라미터를 조회하는 것은 `@RequestParam` `@ModelAttribute` 를 사용하면 되고, Http 메시지 Body를 바로 읽을 때는 `@ResponseBody`를 바로 사용하면 된다.
@ResponseBody는 응답 결과를 Http 메시지 Body에 직접 담아서 반환할 수 있기 때문이다. 


#### JSON

```java
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData helloData) {
    log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
    return "good";
}
```

@RequestBody에 String 뿐만이 아니라 HelloData 처럼 객체를 넣을 수 있다.

현재 Http 메시지 Body에 {"username":"hello", "age":20} , content-type: application/json 이 정보가 넘어온다고 가정하자.
그러면 Http message converter가 넘어오는 정보의 content-type이 json이기 때문에 객체에 맞는 것으로 반환해주는데,
MappingJacksonHttpMessageConverter가 동작을 해서 이것이 `objectMapper.readValue(messageBody, HelloData.class);` 이 코드를 
대신 실행해준다. 즉 Http message converter 가 앞서 content-type이 json이었기 때문에  `objectMapper.readValue(messageBody, HelloData.class);` 이 코드를 대신 실행시킨다는 것이다.

추가로 @RequestBody는 생략이 불가능하다.
위에서 이런 내용이 있었다. String , int , Integer 같은 단순 타입은 @RequestParam을 적용하고, 나머지는 @ModelAttribute(argument resolver로 지정해둔 타입 외)을 적용한다,
따라서 이 경우 HelloData에 @RequestBody를 생략해버리면 @ModelAttribute가 적용된다. 
@ModelAttribute HelloData data. 이렇게 되면 Http 메시지 Body가 아니라 처음에 공부했던 요청 파라미터를 처리하게 된다.
그래서 username=null,age=0 이 log에 찍혔던 것이다.

따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.

추가로 반환 타입을 객체로 하여서 HelloData 객체가 Http message converter에 의해서 json으로 바뀌어서 바뀐 json문자가 Http 메시지 응답에 넣어져서 응답으로 나간다. 
```java
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
    log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
    return helloData;
}
```
postman으로 POST요청을 보내본 결과 아래와 같이 결과가 나왔다. 
```json
{
    "username": "hello",
    "age": 240
}
```
json이 객체가 되었다가, 객체가 반환될 때 다시 json이 되서 저렇게 응답이 나온 것이다.

- @RequestBody 요청
  - JSON 요청 -> Http message converter -> 객체
  - JSON 으로 요청한 것이 JSON을 처리하는 Http message converter가 실행이 되서 객체로 바꾸고 그것이 인자 data(@RequestBody HelloData data)로 전달된다.

- @ResponseBody 응답
  - 객체 -> Http message converter -> JSON 응답
  - 응답은 객체가 JSON을 처리하는 Http message converter가 실행이 되서 JSON으로 응답을 해서 나가게 된다.

잠깐 헷갈리는 부분이 있어서 @RequestBody를 사용할 때와 HttpEntity를 사용할 때의 차이점을 다시 정리해보았다.
@RequestBody는 HTTP 요청 본문의 내용만 객체로 변환하여 처리하는 데 사용된다.
HttpEntity는 HTTP 요청의 본문과 헤더 모두에 액세스하려는 경우에 사용된다.

- @RestController => @Controller + @ResponseBody
View 템플릿을 사용하는 것이 아니라, HTTP 메시지 Body에 직접 데이터를 입력한다. 
@RestController 글자 대로 **Rest API(HTTP API)를 만들 때 사용하는 컨트롤러**이다.
참고로 @ResponseBody 는 클래스 레벨에 추가하면 전체 메서드에 적용된다. 







