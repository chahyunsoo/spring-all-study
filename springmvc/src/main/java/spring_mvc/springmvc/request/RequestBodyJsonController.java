package spring_mvc.springmvc.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import spring_mvc.springmvc.basic.HelloData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody={}", messageBody);
//        System.out.println("messagebody:" + messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
//        objectMapper.readValue() 는 JSON 문자열을 특정 Java 클래스 타입의 객체로 변환하는 역할.

        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws JsonProcessingException {
        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
        return "good";
    }

    /**
     * 그냥 header 정보 조회하기 위해서 인자에 HttpEntity 추가해서 한번 해본 것.
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData, oData.getAge());
        HttpHeaders headers = httpEntity.getHeaders();
        log.info("headers={}", headers);
        return "good";
    }
    */


    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username={}.age={}", data.getUsername(), data.getAge());
        return "test";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
        return helloData;
        //HelloData 객체가 Http message converter에 의해서 json으로 바뀌어서 바뀐 json문자가 Http 메시지 응답에 넣어져서 응답으로 나간다.
        /**
         * 결과가 이렇게 나온다.
         * {
         *     "username": "hello",
         *     "age": 240
         * }
         */
    }
}
