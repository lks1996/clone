package toy.review.service;

import org.springframework.transaction.annotation.Transactional;
import toy.review.domain.Board;
import toy.review.domain.Comments;
import toy.review.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.valueOf;

@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /* 게시글 등록 */
    public String registration(Board board) {
        boardRepository.save(board);
        return board.getTitle();
    }


    /* 전체 게시글 조회 */
    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    /* 특정 게시글 검색(아이디로) */
    public Board findOneBoardById(Long board_id) {
        return boardRepository.findByBoardId(board_id);
    }

    /* 특정 게시글 검색(제목으로) */
    public List<Board> findBoardByTitle(String keyword) {
        List<Board> searchedBoardList = new ArrayList<>();

        List<Board> boardList = boardRepository.findAll();

        for (int i = 0; i < boardList.size(); i++) {

            if (boardList.get(i).getTitle() == null || boardList.get(i).getTitle().isEmpty()) {
                continue;
            }
            if (boardList.get(i).getTitle().contains(keyword)) {
                Board board = new Board();
                board.setBoard_id(boardList.get(i).getBoard_id());
                board.setTitle(boardList.get(i).getTitle());
                board.setWriter(boardList.get(i).getWriter());
                board.setContents(boardList.get(i).getContents());
                board.setRegister_date(boardList.get(i).getRegister_date());
                searchedBoardList.add(board);

                System.out.println("boardList = " + boardList.get(i).getBoard_id());
                System.out.println("boardList @@@@= " + boardList.get(i).getTitle());
                System.out.println("boardList = " + boardList.get(i).getWriter());
                System.out.println("boardList = " + boardList.get(i).getContents());
            }
        }
        return searchedBoardList;
    }

    public List<Board> pagingBoard(int startIndex, int pageSize) {
        return boardRepository.findWithPaging(startIndex, pageSize);
    }

    public List<Board> pagination(List<Board> boards, int startIndex, int pageSize) {
        List<Board> PagedBoardList = new ArrayList<>();
        int lastIndex = startIndex + pageSize;

        System.out.println("!!!!!!!!!!!!startIndex = " + startIndex);
        System.out.println("!!!!!!!!!!!!startIndex + pageSize = " + (startIndex+pageSize) );

        for(int i = startIndex; i < lastIndex; i++){
            Board board = new Board();
            board.setBoard_id(boards.get(i).getBoard_id());
            board.setTitle(boards.get(i).getTitle());
            board.setWriter(boards.get(i).getWriter());
            board.setContents(boards.get(i).getContents());
            board.setRegister_date(boards.get(i).getRegister_date());
            PagedBoardList.add(board);
        }
        return PagedBoardList;
    }

    public Comments registerComments(Comments comments) {
        return boardRepository.saveComments(comments);
    }

    public List<Comments> getComments(Long board_id) {
        return boardRepository.findAllComments(board_id);
    }
}
