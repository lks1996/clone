package toy.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toy.review.repository.BoardRepository;
import toy.review.repository.JdbcBoardRepository;
import toy.review.repository.JdbcMemberRepository;
import toy.review.repository.MemberRepository;
import toy.review.service.BoardService;
import toy.review.service.LoginService;
import toy.review.service.MemberService;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class Config {
    private final DataSource dataSource;
    public Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }
//    private final MemberRepository memberRepository;
//
//    @Autowired
//    public Config(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    @Bean
    public LoginService loginService() {
        return new LoginService(memberRepository());
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public BoardService boardService(){
        return new BoardService(boardRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
          return new JdbcMemberRepository(dataSource);
//          return new JpaMemberRepository(em);
      }

    @Bean
    public BoardRepository boardRepository() {
        return new JdbcBoardRepository(dataSource);
    }
}
