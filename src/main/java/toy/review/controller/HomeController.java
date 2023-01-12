package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import toy.review.domain.Member;
import toy.review.service.MemberService;
import toy.review.service.weatherModule;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    private final MemberService memberService;

    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(@CookieValue(name = "memberId", required = false) String memberId,
                       Model model) {

        if (memberId == null) {
            return "home";
        }

        // 쿠키에 대응되는 멤버 있는지 확인
        Optional<Member> loginMember = memberService.findOne(memberId);

        log.info("loginMember" + loginMember.get().getUser_id());

        if (loginMember == null) {
            return "home";
        }

        //정상 로그인
        String loginMemberName = loginMember.get().getUser_id();
        model.addAttribute("loginMemberName", loginMemberName);
        return "loginHome";
    }
}



