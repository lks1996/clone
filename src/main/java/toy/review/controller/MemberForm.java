package toy.review.controller;

import javax.validation.constraints.NotEmpty;

public class MemberForm {
    //@NotEmpty(message="이름을 입력해주세요")
    private String name;
    //@NotEmpty(message="ID를 입력해주세요")
    private String user_id;
    //@NotEmpty(message="비밀번호를 입력해주세요")
    private String user_pw;
    //@NotEmpty(message="비밀번호를 입력해주세요")
    private String user_pw2;

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
