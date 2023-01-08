package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import toy.review.domain.Member;
import toy.review.domain.MemberDTO;
import toy.review.service.LoginService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginProcess(@Valid @ModelAttribute MemberDTO dto, BindingResult bindingResult,
                               HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult.hasErrors()");
            return "login/";
        }

        String inputId = dto.getUser_id();
        String inputPassword = dto.getUser_pw();

        Member loginMember = LoginService.loginOrCheckPwd(inputId, inputPassword);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            log.info("loginMember == null");

            return "login/loginForm";
        }

        Cookie cookie = new Cookie("memberId", String.valueOf(loginMember.getUser_id()));
        response.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expiredCookie(response, "memberId");
        return "redirect:/";
    }
    private void expiredCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
