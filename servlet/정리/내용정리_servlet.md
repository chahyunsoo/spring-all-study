### Web Server , Web Application Server(WAS)

WAS는 Web Server의 기능을 모두 포함히지만,
프로그램 코드를 수행해서 애플리케이션 로직을 수행할 수 있다는 점이 크다.
Web Server는 정적인 파일을 들고 제공을 하기 때문에 HTML을 특정 사용자마다 다른 화면을 보여주거나 그런 것을 할 수가 없었는데,
WAS는 프로그래밍(프로그램 코드를 수행)을 할 수 있기 때문에 사용자마다 다른 정보를 보여줄 수 있다.
대표적으로 Tomcat, Jetty, Undertow와 같은 서버들이 있다.

결론: 웹 서버는 정적 리소스(파일), WAS는 애플리케이션 로직

WAS는 애플리케이션 코드를 실행하는데 더 특화

WAS 하나만으로 정적 리소스 파일과 무거운 비즈니스 로직을 처리하기엔 부담이 된다. WAS가 장애가 날시에 오류 화면 조차도
안뜰 수 있기 때문에, 'Web Server와 WAS를 같이 사용' 한다. 정적 리소스 파일과 같이 가벼운 파일들은
Web Server에서 처리를 하고, Web Server에서 처리하지 못하면 WAS로 넘겨서 중요한 애플리케이션 로직을 처리하는데에만 집중할 수 있다.
추가로 정적 리소스만 제공하는 Web Server는 잘 죽지 않고, 애플리케이션 로직이 동작하는 WAS 서버는 잘 죽는다.
그래서 WAS와 DB 장애시 Web Server가 오류 화면을 제공할 수 있다.

### 서블릿

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response){
    //애플리케이션 로직
    } 
}
```

- urlPatterns(/hello)의 URL이 호출되면 서블릿 코드가 실행
- HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
- HTTP 응답 정보를 편리하게 제공할 수 있는 HttpServletResponse
- 개발자는 HTTP 스펙을 매우 편리하게 사용

HTTP 요청시
- WAS는 Request, Response 객체를 새로 만들어서 서블릿 객체 호출
- 개발자는 Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
- 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력
- WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성


#### 서블릿, 서블릿 컨테이너

- 톰캣처럼 서블릿을 지원하는 WAS를 서블릿 컨테이너라고 함
- 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 싱글톤으로 관리
    - 고객의 요청이 올 때 마다 계속 객체를 생성하는 것은 비효율
    - 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
    - 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
    - 공유 변수 사용 주의
    - 서블릿 컨테이너 종료시 함께 종료
- JSP도 서블릿으로 변환 되어서 사용
- 동시 요청을 위한 멀티 쓰레드 처리 지원

### Thread Pool

- 특징
    - 필요한 쓰레드를 쓰레드 풀에 보관하고 관리한다.
    - 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리한다. 톰캣은 최대 200개 기본 설정 (변경 가능) 사용

- 사용
    - 쓰레드가 필요하면, 이미 생성되어 있는 쓰레드를 쓰레드 풀에서 꺼내서 사용한다.
    - 사용을 종료하면 쓰레드 풀에 해당 쓰레드를 반납한다.
    - 최대 쓰레드가 모두 사용중이어서 쓰레드 풀에 쓰레드가 없으면?
        - 기다리는 요청은 거절하거나 특정 숫자만큼만 대기하도록 설정할 수 있다.

- 장점
    - 쓰레드가 미리 생성되어 있으므로, 쓰레드를 생성하고 종료하는 비용(CPU)이 절약되고, 응답 시간이 빠르다.
    - 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리할 수 있다.

WAS의 주요한 튜닝 포인트는 '최대 쓰레 수' 이다.
- 너무 낮게 설정시 -> 동시 요청이 많으면, 서버 리소스는 여유롭지만 클라이언트의 응답이 지연
- 너무 높게 설정시 -> 동시 요청이 많으면, CPU, 메모리 리소스 임계점 초과로 서버가 다운
- 장애가 발생하면? -> 클라우드면 일단 서버를 늘리고 이후에 튜닝, 클라우드가 아니면 튜닝부터


`멀티 쓰레드에 대한 부분은 WAS가 처리`
<br>개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨
<br>**개발자는 마치 싱글 쓰레드 프로그래밍을 하듯이 편리하게 소스 코드를 개발**
<br>멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용

### SSR , CSR

- SSR - 서버 사이드 렌더링
    - HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
    - 주로 정적인 화면에 사용
    - 관련기술: JSP, 타임리프 -> 백엔드 개발자
    - 최종적으로 HTML을 서버에서 만듬, 웹 브라우저는 다 생성된 것을 보여주기만 함

- CSR - 클라이언트 사이드 렌더링
    - HTML 결과를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성해서 적용
    - 주로 동적인 화면에 사용, 웹 환경을 마치 앱 처럼 필요한 부분부분 변경할 수 있음 예) 구글 지도, Gmail, 구글 캘린더
    - 관련기술: React, Vue.js -> 웹 프론트엔드 개발자
    - React, Vue.js를 CSR + SSR 동시에 지원하는 웹 프레임워크도 있음
    - SSR을 사용하더라도, 자바스크립트를 사용해서 화면 일부를 동적으로 변경 가능


### HTTP 요청 데이터 
HTTP 요청 메시지를 통해서 클라이언트에서 서버로 데이터를 전달하는 방법
 
- GET - 쿼리 파라미터
  - /url?username=hello&age=20
  - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
- POST - HTML Form
  - content-type(Body에 대한 정보에 대해 설명, 어떤 스타일의 데이터인지..): application/x-www-form-urlencoded
  - 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20 예) 회원 가입, 상품 주문, HTML Form 사용
- HTTP message body에 데이터를 직접 담아서 요청 HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용 POST, PUT, PATCH

#### GET 쿼리 파라미터 

[복수 파라미터에서 단일 파라미터 조회]

```java
        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator()
                        .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        
        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String duplicateName : usernames) {
            System.out.println("username = " + duplicateName);
        }
```
<br>username=hello&username=cha 와 같이 파라미터 이름은 하나 인데, 값이 중복 이면 어떻게 될까?
<br>request.getParameter() 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용 해야 한다.
<br>지금처럼 중복일 때는 request.getParameterValues() 를 사용 해야 한다는 소리이다.
<br>참고로 이렇게 중복일 때 request.getParameter() 를 사용 하면 request.getParameterValues() 의 첫 번째 값을 반환 한다.
<br>하지만 이렇게 중복으로 보내는 경우는 별로 없는 것 같다.

#### POST HTML Form
HTML의 Form을 사용해서 클라이언트에서 서버로 데이터를 전송하는 방법
<br>회원 가입이나, 상품 주문 폼에서 주로 사용

메시지 Body에 데이터가 들어가기 때문에 content-type이 있다.
- content-type: application/x-www-form-urlencoded
- 메시지 Body에 쿼리 파리미터 형식으로 데이터를 전달한다. username=hello&age=20

POST의 HTML Form을 전송하면 웹 브라우저는 다음 형식으로 HTTP 메시지를 만든다.(웹의 개발자 도구를 보면)
- 요청 URL: http://localhost:8080/request-param
- content-type: application/x-www-form-urlencoded 
- message body: username=hello&age=20

content-type은 HTTP 메시지 Body의 데이터 형식을 지정한다.

> GET URL 쿼리 파라미터 형식으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 Body를 사용하지 않기 때문에 content-type이 없다.

> POST HTML Form 형식으로 데이터를 전달하면 **HTTP 메시지 Body에 해당 데이터를 포함해서 보내**기 때문에
Body에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다. 
이렇게 폼으로 데이터를 전송하는 형식을 application/x-www-form-urlencoded 라 한다.


#### API 메시지 Body - 단순 텍스트

- HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용, JSON, XML, TEXT 
- 데이터 형식은 주로 JSON 사용
- POST, PUT, PATCH

HTTP 메시지 바디의 데이터를 InputStream를 통해서 직접 읽을 수 있다.
```java
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //메시지 Body의 내용을 바이트 코드로 바로 얻을 수 있다.
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
```

- inputStream은 byte 코드를 반환한다. byte 코드를 우리가 읽을 수 있는 문자(String)로 보려면 문자표(Charset)를 지정해주어야 한다. 
여기서는 UTF_8 Charset을 지정해주었다.

- 문자 전송
  - POST http://localhost:8080/request-body-string 
  - content-type: text/plain
  - message body: hello
  - 결과: messageBody = hello


#### API 메시지 Body - JSON

- JSON 형식 전송
  - POST http://localhost:8080/request-body-json 
  - content-type: application/json
  - message body: {"username": "hello", "age": 20} 
  - 결과: messageBody = {"username": "hello", "age": 20}

보통 JSON을 그대로 사용하지 않기 때문에, JSON형식을 파싱할 수 있도록 
일단 객체를 만들었다.
```java
@Getter
@Setter
public class HelloData {

    private String username;
    private int age;

}
```
그리고 JSON을 전달받아 객체로 변환하는 과정을 거쳐야 한다.
```java
@WebServlet(name = "requestBodyJsonServlet",urlPatterns ="/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //메시지 Body의 내용을 바이트 코드로 바로 얻을 수 있다.
      ServletInputStream inputStream = request.getInputStream();
      String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

      HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
      System.out.println("helloData.getUsername()= " + helloData.getUsername());
      System.out.println("helloData.getAge() = " + helloData.getAge());
    }
[실행 결과]
messageBody = {
    "username":"name",
    "age":20
}
helloData.getUsername()= name
helloData.getAge() = 20
```
JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다. 
스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리( ObjectMapper )를 함께 제공한다.


### HTTP 응답 데이터

- 단순하게 텍스트 응답
  - writer.println("ok");
  
- HTML로 응답 
  - HTTP 응답으로 HTML을 반환할 때는 content-type을 text/html 로 지정해야 한다.
  
- HTTP API - MessageBody JSON 응답
  - HTTP 응답으로 JSON을 반환할 때는 content-type을 application/json 로 지정한다.
    Jackson 라이브러리가 제공하는 objectMapper.writeValueAsString() 를 사용하게 되면 객체를 JSON 문자로 변경 가능.
  

















  