package toy.review.repository;

import toy.review.domain.Board;
import toy.review.domain.Comments;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);

    Board findByBoardId(Long board_id);

    List<Board> findAll();

    List<Board> findByTitle(String keyword);

    Comments saveComments(Comments comments);


    List<Comments> findAllComments(Long board_id);
}
