package toy.review.service;

import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import toy.review.domain.Member;
import toy.review.domain.MemberDTO;
import toy.review.repository.MemberRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    public Long join(Member member) {

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public void updateUserInfo(MemberDTO dto) {
        Member member = memberRepository.findByUserId(dto.getUser_id()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 회원입니다."));

        /* 수정한 비밀번호 암호화 */
        //String encryptPassword = encoder.encode(memberDto.getPassword());
        memberRepository.update(dto); // 회원 수정

        //log.info("회원 수정 성공");
    }


    /** 전체 회원 조회 **/
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /** 특정 회원 조회(회원 아이디 통해) **/
    public Optional<Member> findOne(String memberId) {
        return memberRepository.findByUserId(memberId);
    }

    /** 중복 회원 검증 **/
    private void validateDuplicateMember(Member member) {
        memberRepository.findByUserId(member.getUser_id())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /** 닉네임 중복 체크 **/
    public boolean checkNickname(String user_id, String name) {
        Optional<Member> isExistMember = memberRepository.findByName(name);

        //해당 닉네임을 가진 Member가 db에 존재한다면
        if(isExistMember.isPresent()){

            String ExistMemberId = isExistMember.get().getUser_id();

            if(Objects.equals(ExistMemberId, user_id)){

                System.out.println("닉네임을 그대로 입력함.");
                return false;

            } else {
                return true;
            }
        } else {
            System.out.println("이상 없음");
            return false;
        }
    }



}
