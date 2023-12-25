package spring.lectureA_2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name",unique = true)
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore //Entity에 의존관계가 들어와야 하는데 지금 의존관계가 나가는 상태
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}