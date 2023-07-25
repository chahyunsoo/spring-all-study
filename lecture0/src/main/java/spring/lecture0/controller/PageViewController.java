package spring.lecture0.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "lecture/lecture-title";
        /*
        랜더링, 뷰 리졸버(viewResolver)가 화면을 찾아서 처리
        'resources: templates/' + {ViewName} + '.html'
         */
    }

    @GetMapping("lecture-number")
    public String lectureZero(@RequestParam(value = "step", required = false) Integer id, Model model) {
        model.addAttribute("step", id);
        return "lecture/lecture-number";
    }

    @GetMapping("lecture-date")
    @ResponseBody //Response 응답 Body부분에 직접 넣어주겠다
    public String lectureDate(@RequestParam(value = "practicedDate", required = false) String date) {
        return "강의 수강 Month: " + date;
        //date에 7월이라는 문자열을 넣으면 -> "강의 수강 Month: 7월" 로 바뀜,
        //date문자열이 요청한 클라이언트에 그대로 내려감, 소스보기보면 HTML태그가 하나도 없다
    }

    //만약에 데이터를 내놓아라...이것때문에 API방식을 많이씀, 객체를 반환
    @GetMapping("lecture-api")
    @ResponseBody
    public LecApi lectureApi(@RequestParam(value = "name", required = false) String name) {
        LecApi lecApi = new LecApi();
        lecApi.setName(name);
        return lecApi;  //객체를 넘긴 상태임, JSON 방식

    }
    @Getter
    @Setter
    static class LecApi {
        private String name;
    }

    @GetMapping("sign")
    public String sign() {
        return "sign.html";
    }


}

