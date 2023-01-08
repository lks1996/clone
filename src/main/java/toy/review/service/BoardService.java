package toy.review.service;

import org.springframework.transaction.annotation.Transactional;
import toy.review.domain.Board;
import toy.review.repository.BoardRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public String registration(Board board) {
        boardRepository.save(board);
        return board.getTitle();
    }

//    private void validateBoard(Board board) {
//
//        }
//
//
//    }

    //전체 게시글 조회
    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> findOneBoard(Long boardId) {
        return boardRepository.findByBoardId(boardId);
    }
}
