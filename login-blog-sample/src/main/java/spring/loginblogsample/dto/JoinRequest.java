package spring.loginblogsample.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.loginblogsample.domain.User;
import spring.loginblogsample.domain.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = "로그인 아이디를 반드시 입력해주세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 반드시 입력해주세요")
    private String password;

    private String passwordCheck;

    @NotBlank(message = "별명을 반드시 입력해주세요")
    private String nickname;

    //암호화되지 않은 비밀번호
    public User toEntityWithOutEncodedPassword() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .userRole(UserRole.USER)
                .build();
////        member.getMemberRoleSet().add(MemberRole.USER);
////        member.getMemberRoleSet().add(MemberRole.ADMIN);
    }

    public User toEntityWithOutEncodedPasswordForAdmin() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .userRole(UserRole.ADMIN)
                .build();
////        member.getMemberRoleSet().add(MemberRole.USER);
////        member.getMemberRoleSet().add(MemberRole.ADMIN);
    }
//
//    //암호화된 비밀번호
//    public User toEntityWithEncodedPassword(String encodedPassword) {
//        User user = User.builder()
//                .loginId(this.loginId)
//                .password(encodedPassword)
//                .nickname(this.nickname)
//                .userRole(UserRole.USER)
//                .build();
////        member.getMemberRoleSet().add(MemberRole.USER);
////        member.getMemberRoleSet().add(MemberRole.ADMIN);
//        return user;
//    }

}
