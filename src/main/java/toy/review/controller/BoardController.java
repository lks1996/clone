package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toy.review.domain.Board;
import toy.review.domain.Comments;
import toy.review.domain.Member;
import toy.review.service.BoardService;
import toy.review.service.MemberService;

import java.sql.Timestamp;
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
    public String create(BoardForm form) {

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
        Board boardResult = boardService.findOneBoardById(bno);
        model.addAttribute("oneBoard", boardResult);

        /** comments 가져오기 작성할 것 **/
        List<Comments> comments = boardService.getComments(bno);
        model.addAttribute("comments", comments);

        List<Board> boards = boardService.findAllBoards();
        model.addAttribute("boards", boards);

        return "board/view";
    }

    @PostMapping("/board/regcomments")
    @ResponseBody
    public Boolean registerComments(@RequestBody Comments commentsDTO,
                                    @CookieValue(name = "memberId", required = false) String memberId) {

        Date timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String now_dt = sdf.format(timestamp);

        Comments comments = new Comments();
        comments.setWriter_id(memberId);
        comments.setComment_register_date(now_dt);
        comments.setComment_contents(commentsDTO.getComment_contents());
        comments.setBoard_id(commentsDTO.getBoard_id());

        boardService.registerComments(comments);

        return true;
    }

    @GetMapping("/board/search")
    public String searchBoard(String keyword, Model model) {
        List<Board> boards = boardService.findBoardByTitle(keyword);

        model.addAttribute("boards", boards);
        return "board/searchBoard";
    }
}
