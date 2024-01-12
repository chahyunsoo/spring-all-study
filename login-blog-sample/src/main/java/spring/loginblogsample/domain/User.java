package spring.loginblogsample.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "nick_name")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


//    @ElementCollection(targetClass = MemberRole.class)
//    @JoinTable(name = "roles", joinColumns = @JoinColumn(name = "member_id"))
//    @Column(name = "member_role_type")
//    @NotNull(message = "반드시 값이 존재해야 합니다.")
//    private Set<MemberRole> memberRoleSet = new HashSet<>();

    @Builder
    public User(String loginId, String nickname, String password, UserRole userRole) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

}
