### 응답

#### 정적 리소스, View 템플릿 

- 정적 리소스
  - 웹 브라우저에 정적인 html,css,,js 를 제공할때는 정적 리소스를 사용한다. 파일을 그대로 전달하는 것을 말한다.
  - src/main/resource경로에 존재하는 폴더들은 정적으로 스프링 부트가 내장 톰캣 통해서 자동으로 제공을 해준다. 이것이 적용되는 폴더들은
    /static , /public , /resources , /META-INF/resources 가 된다.
  - src/main/resources/static/test/hello.html 경로에 파일이 들어있다면, localhost:8080/test/hello.html 라고 입력하면 된다.


- View 템플릿 사용
  - 웹 브라우저에 서버가 동적인 html을 제공할 때는 View 템플릿을 사용한다.(서버 사이드 랜더링,SSR)
  - View 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다.
  - View 템플릿 경로는 /src/main/resources/templates


- Http 메시지 사용
  - html이 아닌 데이터를 전달하는 것이기 때문에, Http 메시지 Body에 JSON같은 데이터를 실어서 응답한다.
  - 

위의 3가지 방식은 응답 데이터를 만드는 3가지 방식들이다.

- 정리
  - @ResponseBody 가 없으면 response/hello 로 View 리졸버가 실행되어 View를 찾고, 렌더링 한다. 
  - @ResponseBody 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 response/hello 라는 문자가 입력된다.
  그래서 View의 논리 이름인 response/hello 를 반환했을때 이 경로의 뷰 템플릿이 렌더링 되는 것을 확인할 수 있다.
  - Void를 반환하는 경우
  @Controller 를 사용하고, HttpServletResponse , OutputStream(Writer) 같은 HTTP 메시지 바디를 처리하는 파라미터가 없으면 
  요청 URL을 참고해서 논리 뷰 이름으로 사용 
    - 요청 URL: /response/hello
    - 실행: templates/response/hello.html
  하지만 이 방식은 좋은 방식은 아닌 것 같다.
  - @ResponseBody, HttpEntity를 사용하면, View 템플릿을 사용하여 View를 랜더링 해주는 것이 아니라, Http 응답 메시지 Body에 바로 데이터를 넣어서 출력할 수 있다.
  정말 중요하지만 내가 헷갈리는 개념이었다. 어노테이션이 너무 많기도 하고 비슷비슷한 느낌때문에 어노테이션의 차이를 확실하게 짚고 넘어가는 것이 중요한 것 같다.


#### HTTP API, 메시지 바디에 직접 입력

