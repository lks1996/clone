package toy.review.domain;

public class Member {

    private Long id;
    private String name;

    private String user_id;

    private String user_pw;
    private String user_pw2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_pw2() {
        return user_pw2;
    }
    public void setUser_pw2(String user_pw2) {
        this.user_pw2 = user_pw2;
    }
}
