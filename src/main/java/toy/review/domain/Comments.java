package toy.review.domain;

public class Comments {

    private Long comment_id;
    private String writer_id;
    private String comment_register_date;
    private String comment_contents;
    private Long board_id;

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getComment_register_date() {
        return comment_register_date;
    }

    public void setComment_register_date(String comment_register_date) {
        this.comment_register_date = comment_register_date;
    }

    public String getComment_contents() {
        return comment_contents;
    }

    public void setComment_contents(String comment_contents) {
        this.comment_contents = comment_contents;
    }

    public Long getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Long board_id) {
        this.board_id = board_id;
    }
}
