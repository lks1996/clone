package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import toy.review.domain.Board;
import toy.review.domain.Member;
import toy.review.service.BoardService;
import toy.review.service.MemberService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @Autowired
    public BoardController(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @GetMapping("/board")
    public String showBoard(Model model) {
        List<Board> boards = boardService.findAllBoards();
        model.addAttribute("boards", boards);
        return "board/showBoard";
    }

    @GetMapping("/board/new")
    public String createBoard(@CookieValue(name = "memberId", required = false) String memberId,
                              Model model) {
        if (memberId == null) {
            log.info("로그인 후 게시글 작성 가능");
            return "login/loginForm";
        }
        // 쿠키에 대응되는 멤버 있는지 확인
        Optional<Member> loginMember = memberService.findOne(memberId);

        log.info("Board_loginMember" + loginMember.get().getUser_id());

        if (loginMember == null) {
            return "home";
        }

        String loginMemberName = loginMember.get().getUser_id();
        model.addAttribute("loginMemberName", loginMemberName);
        return "board/createBoard";

    }

    @PostMapping("/board/new")
    public String create(BoardForm form) throws ParseException {

        Date timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String now_dt = sdf.format(timestamp);

        log.info(now_dt);

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContents(form.getContents());
        board.setWriter(form.getWriter());
        board.setRegister_date(now_dt);

        System.out.println("form.getWriter() = " + form.getWriter());
        boardService.registration(board);

        return "redirect:/board";
    }

    @GetMapping("/board/view/{bno}")
    public String viewBoard(@PathVariable Long bno, Model model) {
        Optional<Board> boardResult = boardService.findOneBoard(bno);

        System.out.println("boardResult = " + boardResult);
        System.out.println("boardResult.getBoard_id = " + boardResult.get().getBoard_id());

        Long boardResult_board_id = boardResult.get().getBoard_id();
        String boardResult_title = boardResult.get().getTitle();
        String boardResult_contents = boardResult.get().getContents();
        String boardResult_writer = boardResult.get().getWriter();
        String boardResult_register_date = boardResult.get().getRegister_date();


        model.addAttribute("boardResult_board_id", boardResult_board_id);
        model.addAttribute("boardResult_title", boardResult_title);
        model.addAttribute("boardResult_contents", boardResult_contents);
        model.addAttribute("boardResult_writer", boardResult_writer);
        model.addAttribute("boardResult_register_date", boardResult_register_date);

        return "board/view";
    }
}
