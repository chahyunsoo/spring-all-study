package spring.loginblogsample.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id")
    private Long loginId;

    @Column(name = "nick_name")
    private String nickname;

    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass = MemberRole.class)
    @JoinTable(name = "roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "member_role_type")
    @NotNull(message = "반드시 값이 존재해야 합니다.")
    private Set<MemberRole> memberRoleSet = new HashSet<>();

}
