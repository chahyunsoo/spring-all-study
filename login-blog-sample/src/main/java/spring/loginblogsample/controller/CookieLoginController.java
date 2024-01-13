package spring.loginblogsample.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/cookie-login")
public class CookieLoginController {
    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(@CookieValue(name = "idByCookie", required = false) Long memberId, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        User user = userService.returnByUserId(memberId);
        if (user != null) {
            model.addAttribute("nickname", user.getNickname());
        }
        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식 시발");
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute JoinRequest joinRequest, BindingResult bindingResult) {
//        model.addAttribute("loginType", "cookie-login");
//        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식 병신아");

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

//        // password와 passwordCheck가 같은지 체크
//        // Null 체크를 추가하여 NullPointerException 방지
//        if (joinRequest.getPassword() != null && !joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
//            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
//        }

//        if (bindingResult.hasErrors()) {
//            return "join";
//        }
        userService.joinWithOutEncodedPassword(joinRequest);

        System.out.println("joinRequest = " + joinRequest);
        System.out.println("joinRequest.getLoginId() = " + joinRequest.getLoginId());
        System.out.println("joinRequest.getNickName() = " + joinRequest.getNickname());
        System.out.println("joinRequest.getPassword() = " + joinRequest.getPassword());

        return "redirect:/cookie-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");
        model.addAttribute("loginRequest", new JoinRequest());

        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletResponse httpServletResponse
            , Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        User loginMember = userService.login(loginRequest);
        if (loginMember == null) {
            bindingResult.reject("loginFail", "로그인 아이디 혹은 비밀번호가 틀렸습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "login";
        }

        //로그인 성공 시 쿠키 생성
        Cookie idByCookie = new Cookie("idByCookie", String.valueOf(loginMember.getUserId()));
        idByCookie.setMaxAge(60 * 60); //쿠키 유효 시간 1시간
        httpServletResponse.addCookie(idByCookie);

        return "redirect:/cookie-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        Cookie idByCookieIsNull = new Cookie("idByCookie", null);
        idByCookieIsNull.setMaxAge(0);
        httpServletResponse.addCookie(idByCookieIsNull);

        return "redirect:/cookie-login";
    }

    @GetMapping("/info")
    public String getUserInfo(@CookieValue(value = "idByCookie", required = false) Long userId
            , Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        User findByUserId = userService.returnByUserId(userId);
        if (findByUserId == null) {
            return "redirect:/cookie-login/login";
        }

        model.addAttribute("user", findByUserId);
        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(@CookieValue(value = "idByCookie", required = false) Long userId
            , Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키를 사용한 로그인 방식");

        User findByUserId = userService.returnByUserId(userId);

        if (findByUserId == null) {
            return "redirect:/cookie-login/login";
        }

        if (findByUserId.getUserRole() != UserRole.ADMIN) {
            return "redirect:/cookie-login";
        }

        return "admin";
    }
}
