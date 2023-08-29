package spring_mvc.springmvc.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {
    private Logger log = LoggerFactory.getLogger(getClass());

    //특별한 설정(method=RequestMethod.GET 등등)이 없으면 GET,POST,PUT,DELETE 모두 받아들인다. 이 경우에는 4가지 모두 ok를 반환.
    @RequestMapping("/hello-request-v1")
//    @RequestMapping("/hello-request",'hello-request2') {...}  url이 2개가 와도 상관없음, 모두 매
    public String helloRequest_v1() {
        log.info("hellorequest");
        return "ok";
    }


    @RequestMapping(value = "/hello-request-v2", method = RequestMethod.GET)
    public String helloRequest_v2() {
        log.info("add method = RequestMethod.GET");
        return "ok";
    }

    @GetMapping(value = "/hello-request-v3")
    public String helloRequest_v3() {
        log.info("change to @GetMapping");
        return "ok";
    }

    /**
     * PathVariable 사용
     *
     * @return
     * @PathVariable("userId") String userId -> @PathVariable userId
     * 요청 url이 /mapping/userA 이런식으로 url자체에 값이 들어가 있다.
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath_v1(@PathVariable("userId") int dataId) {
        log.info("mapping Path userId={}", dataId);
        return "ok";
    }

    /**
     * 다중 매핑
     *
     * @param dataId
     * @return
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath_v2(@PathVariable("userId") int dataId, @PathVariable("orderId") int dataOrderId) {
        log.info("mapping Path userId={},orderId={}", dataId,dataOrderId);
        return "ok";
    }




    /**
     * params 값이 무조건 들어와야 매핑이 된다. -> localhost:8080/mapping-param?mode=debug
     * 즉, 특정 파라미터 정보가 있어야 호출이 됨.
     * @return
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 이건 특정 headers 조건이 들어와야 한다.
     * @return
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     * <p>
     * consume이 Content-Type
     */
    @GetMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     *
     * HTTP 요청이 ACCEPT랑 맞아야 한다.
     * 아래 코드는 text/html을 생산한다. ACCEPT 타입이 text/html 이어야 한다.
     * 클라이언트 입장에서 ACCEPT라는 것은 이 타입만 받아들일 수 있다라는 뜻.
     * 클라이언트가 나는 Content-Type이 text/html 인 것을 받아들일 수 있다라는 뜻.
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html")
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
