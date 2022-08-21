package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {
    private long id;
    @NotEmpty
    private String loginId;//로그인 아이디
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}
