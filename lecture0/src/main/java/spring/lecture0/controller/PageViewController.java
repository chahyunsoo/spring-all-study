package spring.lecture0.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageViewController {
    /*
    Thymeleaf 템플릿 엔진에서는 문자열과 변수를 결합할 때,
    문자열을 따옴표로 감싸서 명시적으로 문자열임을 표시
    index.html 파일을 따로 맵핑해주지 않아도 초기페이지로 자동으로 감지 한다.
   */
    @GetMapping("lecture")
    public String getLecture(Model model) {
        model.addAttribute("title", "스프링 입문 강의");  //key는 title이고 값은 "스프링 입문 강의"
        return "hello";
        /*
        랜더링, 뷰 리졸버(viewResolver)가 화면을 찾아서 처리
        'resources: templates/' + {ViewName} + '.html'
         */
    }
//    @GetMapping("hello")
//    public String hello(Model model) {
//        model.addAttribute("data", "스프링 입문 강의");
//        return "hello";
//    }
}

