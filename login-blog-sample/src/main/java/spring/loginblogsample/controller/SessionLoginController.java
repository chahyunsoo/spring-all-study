package spring.loginblogsample.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import spring.loginblogsample.domain.User;
import spring.loginblogsample.domain.UserRole;
import spring.loginblogsample.dto.JoinRequest;
import spring.loginblogsample.dto.LoginRequest;
import spring.loginblogsample.service.UserService;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login")
public class SessionLoginController {
    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "userSessionId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션을 사용한 로그인 방식");

        User user = userService.returnUserByUserId(userId);

        if (user != null) {
            System.out.println("user.getNickname() = " + user.getNickname());
            model.addAttribute("nickname", user.getNickname());
        }

        return "home";
    }

    //회원가입
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션을 사용한 로그인 방식");
        model.addAttribute("joinRequest", new JoinRequest());

        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션을 사용한 로그인 방식 병신아");

        System.out.println("JoinRequest.post = " + joinRequest.getNickname());
        //loginId 중복체크
        if (userService.checkDuplicateLoginId(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "동일한 로그인 아이디가 존재합니다.."));
        }

        //닉네임 중복체크
        if (userService.checkDuplicateNickName(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "동일한 닉네임이 존재합니다.."));
        }

        //password랑 passwordCheck가 같은지 체크
        if (joinRequest.getPassword() != null && !joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.joinWithOutEncodedPassword(joinRequest);

        return "redirect:/session-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("loginRequest", new LoginRequest());

        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest, BindingResult bindingResult
            , Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginUser = userService.login(loginRequest);

        if (loginUser == null) {
            bindingResult.reject("loginFail", "로그인 아이디 혹은 비밀번호가 틀렸습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        /**
         * 로그인 성공 시 -> 세선 성공
         */
        //세션을 생성하기 전에 -> 기존에 존재하던 세션을 파기함
        httpServletRequest.getSession().invalidate();

        //세션이 없으면 세션을 생성함
        HttpSession session = httpServletRequest.getSession(true);
        System.out.println("session.getId() = " + session.getId());

        //세션에 Key 넣음
        session.setAttribute("userSessionId", loginUser.getUserId());
        //세션 유효시간 30분으로 설정
        session.setMaxInactiveInterval(1800);
        sessionList.put(session.getId(), session);
        System.out.println("sessionList = " + sessionList);



        return "redirect:/session-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            sessionList.remove(session.getId());
            session.invalidate();
        }

        return "redirect:/session-login";
    }

    @GetMapping("/info")
    public String userInfo(@SessionAttribute(name = "userSessionId") Long userId, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        User loginUser = userService.returnUserByUserId(userId);

        if (loginUser == null) {
            return "redirect:/session-login/login";
        }
        model.addAttribute("user", loginUser);

        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginUser = userService.returnUserByUserId(userId);

        if (loginUser == null) {
            return "redirect:/session-login/login";
        }

        if (!loginUser.getUserRole().equals(UserRole.ADMIN)) {
            return "redirect:/session-login";
        }

        return "admin";
    }

    public static Hashtable sessionList = new Hashtable();

    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();
        Map<String, String> lists = new HashMap<>();
        while (elements.hasMoreElements()) {
            HttpSession session = (HttpSession) elements.nextElement();
            lists.put(session.getId(), String.valueOf(session.getAttribute("userSessionId")));
        }
        return lists;
    }
}