package toy.review.repository;

import org.springframework.jdbc.datasource.DataSourceUtils;
import toy.review.domain.Board;
import toy.review.domain.Comments;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcBoardRepository implements BoardRepository{

    private final DataSource dataSource;

    public JdbcBoardRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Board save(Board board) {

        String sql = "insert into board(title, writer, contents, register_date) values(?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;//결과를 받음.

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getWriter());
            pstmt.setString(3, board.getContents());
            pstmt.setString(4, board.getRegister_date());

            pstmt.executeUpdate();//db에 실제 쿼리를 날림.
            rs = pstmt.getGeneratedKeys();

            //rs에 값이 있으면, 값을 꺼냄.
            if (rs.next()) {
                board.setBoard_id(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return board;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Board findByBoardId(Long board_id) {
        String sql = "select * from board where board_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, board_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Board board = new Board();
                board.setBoard_id(rs.getLong("board_id"));
                board.setTitle(rs.getString("title"));
                board.setWriter(rs.getString("writer"));
                board.setContents(rs.getString("contents"));
                board.setRegister_date(rs.getString("register_date"));

                return board;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }


    @Override
    public List<Board> findAll() {
        String sql = "select * from board";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Board> boards = new ArrayList<>();
            while(rs.next()) {
                Board board = new Board();
                board.setBoard_id(rs.getLong("board_id"));
                board.setTitle(rs.getString("title"));
                board.setWriter(rs.getString("writer"));
                board.setContents(rs.getString("contents"));
                board.setRegister_date(rs.getString("register_date"));
                boards.add(board);
            }
            return boards;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }




    @Override
    public Comments saveComments(Comments comments) {
        String sql = "insert into comments(writer_id, comment_register_date, comment_contents, board_id) values(?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;//결과를 받음.

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, comments.getWriter_id());
            pstmt.setString(2, comments.getComment_register_date());
            pstmt.setString(3, comments.getComment_contents());
            pstmt.setLong(4, comments.getBoard_id());

            pstmt.executeUpdate();//db에 실제 쿼리를 날림.
            rs = pstmt.getGeneratedKeys();

            //rs에 값이 있으면, 값을 꺼냄.
            if (rs.next()) {
                comments.setBoard_id(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return comments;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Comments> findAllComments(Long board_id) {
        String sql = "select * from comments where board_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, board_id);
            rs = pstmt.executeQuery();
            List<Comments> comments = new ArrayList<>();
            while(rs.next()) {
                Comments comment = new Comments();
                comment.setComment_id(rs.getLong("comment_id"));
                comment.setWriter_id(rs.getString("writer_id"));
                comment.setComment_register_date(rs.getString("comment_register_date"));
                comment.setComment_contents(rs.getString("comment_contents"));
                comment.setBoard_id(rs.getLong("board_id"));

                comments.add(comment);
            }
            return comments;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }





    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
