package jpa.Section5;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Team {
    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;
    @OneToMany(mappedBy = "team") //여기에 값을 넣어봤자 아무일도 벌어지지 않는다. 조회만 할 수 있다.
    private List<Member> members = new ArrayList<>();
}
