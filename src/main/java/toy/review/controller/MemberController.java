package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toy.review.domain.Member;
import toy.review.domain.MemberDTO;
import toy.review.service.LoginService;
import toy.review.service.MemberService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class MemberController {
    //private final Logger LOGGER = LoggerFactory.getLogger(MemberController.class.getName());

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setUser_id(form.getUser_id());
        member.setUser_pw(form.getUser_pw());
        member.setUser_pw2(form.getUser_pw2());
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members/update")
    public String checkPwd() {
        return "members/checkPwdForm";
    }

    /** 회원 수정 전 비밀번호 확인 **/
    @PostMapping("/checkPwd")
    public String checkPassword(@CookieValue(name = "memberId") String memberId,
                                 @RequestParam String user_pw, Model model){

        Member checkPassword = LoginService.loginOrCheckPwd(memberId, user_pw);

        if (checkPassword == null) {
            log.info("비밀번호 확인 실패");
            return "redirect:/members/update";
        }

        log.info("비밀번호 확인 성공");

        Optional<Member> member = memberService.findOne(memberId);
        String memberName = member.get().getName();
        System.out.println("memberName = " + memberName);

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberName", memberName);


        return "members/memberUpdate";
    }

    /** 회원 정보 수정 **/
    @PutMapping("/member")
    @ResponseBody
    public boolean update(@RequestBody MemberDTO dto) {
        //JSON형태로 받게해주는 Annotation이 @RequestBody이다.

        String id = dto.getName();

        System.out.println("ajax로 넘어온 name = " + dto.getName());
        System.out.println("ajax로 넘어온 id = " + dto.getUser_id());
        System.out.println("ajax로 넘어온 pw = " + dto.getUser_pw());

//        log.info("MemberRestController 진입");
        if(memberService.checkNickname(dto.getUser_id(), dto.getName())){
            //log.info("중복 닉네임");
            log.info("해당 닉네임을 사용하는 사용자 id가 존재함.");
            return false;
            }
        else{
            log.info("사용 가능한 닉네임");

            //회원 정보 수정
            memberService.updateUserInfo(dto);



            return true;
        }
    }


    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }

}
