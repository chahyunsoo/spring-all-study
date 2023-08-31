package spring_mvc.springmvc.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import spring_mvc.springmvc.basic.HelloData;

@Slf4j
@Controller
public class ResponseBodyJsonController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView modelAndView = new ModelAndView("response/hello").addObject("data","my");
        return modelAndView;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello");
        return "response/hello"; //@Controller + String을 반환하면 뷰의 논리적인 이름이 된다.
    }

    @RequestMapping("/response/hello") //경로의 이름이랑 바로 위의 메소드에서 반환되는 뷰의 이름이 같을때 void로 반환타입 해도 상관없음.
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello");
    }
}
