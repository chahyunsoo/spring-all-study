package spring_mvc.springmvc.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    /**
     * HttpServletRequest 이용
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody={}", messageBody);
        response.getWriter().write("ok");
    }

    /**
     * InputStream 이용
     * @param inputStream
     * @param responseWriter
     * @throws IOException
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messagebody={}", messageBody);
        responseWriter.write("ok");
    }

    /**
     * HttpEntity 이용
     * @param httpEntity
     * @throws IOException
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        //<String> ? 어 넌 문자구나 그러면 내가 Http Body에 있는 것을 문자로 바꿔서 너한테 넣어줄게!
        //String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); 를 대신 실행해줄게! 라고 하면서
        //Http message converter 가 동작을 한다.
        String messagebody = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();
        //Http 메시지에 있는 Body를 꺼낸다. 변환된 Body를 꺼낼 수 있다.
        //어? 그럼 반환은 어떻게해?
        //반환타입을 HttpEntity<String>으로 하면 된다.

        log.info("messagebody={}", messagebody);
        log.info("headers={}", headers);
        return new HttpEntity<>("ok");
    }

    @PostMapping("/request-body-string-v4")
    public ResponseEntity<String> requestBodyStringV4(RequestEntity<String> httpEntity) throws IOException {
        String messagebody = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();

        log.info("messagebody={}", messagebody);
        log.info("headers={}", headers);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * @RequestBody 이용, Http message Body 읽어서 바로 넣어준다. + @ResponseBody
     * @param messagebody
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/request-body-string-v5")
    public String requestBodyStringV5(@RequestBody String messagebody) throws IOException {
        log.info("messagebody={}", messagebody);
        return "ok";
    }





}
