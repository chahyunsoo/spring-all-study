package spring.loginblogsample.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.loginblogsample.domain.Member;
import spring.loginblogsample.domain.MemberRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = "로그인 아이디를 반드시 입력해주세요")
    private Long loginId;

    @NotBlank(message = "비밀번호를 반드시 입력해주세요")
    private String password;

    private String passwordCheck;

    @NotBlank(message = "별명을 반드시 입력해주세요")
    private String nickname;

    //암호화되지 않은 비밀번호
    public Member toEntityWithOutEncodedPassword() {
        Member member = Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .build();
        member.getMemberRoleSet().add(MemberRole.USER);
//        member.getMemberRoleSet().add(MemberRole.ADMIN);
        return member;
    }

    //암호화된 비밀번호
    public Member toEntityWithEncodedPassword(String encodedPassword) {
        Member member = Member.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .build();
        member.getMemberRoleSet().add(MemberRole.USER);
//        member.getMemberRoleSet().add(MemberRole.ADMIN);
        return member;
    }

}
