package spring_mvc.springmvc.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring_mvc.springmvc.basic.HelloData;

@Slf4j
@Controller
public class RequestModelAttribute {
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

    /**
     * @ModelAttribute로 변경
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttribute_V2(@ModelAttribute HelloData helloData) {
        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);
        return "ok";
    }

    /**
     * @ModelAttribute 를 생략해도 위의 코드와 동일하게 동작한다.
     * @param helloData
     * @return
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v3")
    public String modelAttribute_V3(HelloData helloData) {
        log.info("username={},age={}", helloData.getUsername(), helloData.getAge());
        log.info("helloData={}", helloData);
        return "ok";
    }
}
