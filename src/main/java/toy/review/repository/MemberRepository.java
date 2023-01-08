package toy.review.repository;

import toy.review.domain.Member;
import toy.review.domain.MemberDTO;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    MemberDTO update(MemberDTO dto);

    Optional<Member> findByUserId(String user_id);

    Optional<Member> findById(Long id);

    Optional<Member> findByName(String name);

    List<Member> findAll();
}
