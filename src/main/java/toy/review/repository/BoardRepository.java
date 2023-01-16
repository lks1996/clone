package toy.review.repository;

import toy.review.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);

    Optional<Board> findByBoardId(Long board_id);

    List<Board> findAll();

    List<Board> findByTitle(String keyword);
}
