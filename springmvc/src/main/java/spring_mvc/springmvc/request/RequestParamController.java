package spring_mvc.springmvc.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
//@RestController
public class RequestParamController {
    /**
     * [V1]
     * 반환 타입이 void인 상태에서 response에 직접 값을 넣었다.
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/request-param-v1",method = RequestMethod.GET)
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={},age={}",username,age);

        response.getWriter().write("ok");
    }

    /**
     * [V2]
     * @ResponseBody를 추가한 버전, @ResponseBody  는 HTTP 응답 메세지에 return값을 바로 넣어서 반환한다,.
     * @param myName
     * @param myAge
     * @return
     */
    @ResponseBody //이걸 붙히면 ok라는 문자가 HTTP 응답 메시지에 바로 딱 넣어서 반환한다. @RestController와 같은 역할
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String myName, @RequestParam("age") int myAge) {
        log.info("username={},age={}", myName, myAge);
        return "ok";
        //이렇게 되면 위에 @Controller 어노테이션에 반환값이 String인 메소드여서 뷰 리졸버가 ok라는 뷰를 찾게 된다. 그래서 ok라는 문자를 HTTP 메시지에 바로 넣고 싶으면
        //@Controller -> @RestController로 바꿔도 된다.
        //하지만 지금 취지는 그것이 아니기 때문에, 지금은 ok라는 문자를 HTTP 메시지에 바로 넣고 싶다. 그러면 @ResponseBody 어노테이션을 한번 추가해보자.
    }

    /**
     * [V3]
     * url을 작성할때 HTTP 파라미터 이름이 @RequestParam의 변수 이름과 같으면 생략 가능하다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        //@RequestParam("username") String myName -> @RequestParam String username
        //("username")을 생략할 수 있지만, 그 오른쪽의 변수명을 파라미터에 입력되는 변수명과 똑같게 입력되어야 한다.
        log.info("username={},age={}", username, age);
        return "ok";
    }

    /**
     * [V4]
     * String, int, Integer등 단순 타입이면 @RequestParam 어노테이션 자체를 생략할 수 있다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) { //@RequestParam 어노테이션 자체도 생략할 수 있다. 물론 변수 이름은 파라미터 변수 이름과 같아야 한다.
        log.info("username={},age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam(required=true)가 기본 값, @RequestParam(required=false)로 하면 값을 꼭 입력할 필요는 없다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username, @RequestParam(required = false) Integer age) {
        log.info("username={},age={}", username, age);
        return "ok";
    }

    /**
     * 추가로 @RequestParam(defaultValue="....") 를 넣으면, 파라미터 값이 없으면 그 값으로 defaultValue의 값이 들어온다.
     * @param username
     * @param age
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault (@RequestParam(required = true,defaultValue = "guest") String username,
                                       @RequestParam(required = false,defaultValue = "-1") int age) {
        //username 값이 없으면 guest가 값이 된다는 뜻.
        log.info("username={},age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터를 Map으로 조회
     * @param paramMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={},age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }


}
