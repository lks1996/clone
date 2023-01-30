package toy.review.service;

import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import toy.review.domain.Member;
import toy.review.repository.MemberRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class LoginService {
    private static MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public static Member loginOrCheckPwd(String inputId, String inputPassword) {
        Optional<Member> findMember = memberRepository.findByUserId(inputId);
        return findMember.filter(m -> m.getUser_pw().equals(inputPassword)).orElse(null);
    }
}
