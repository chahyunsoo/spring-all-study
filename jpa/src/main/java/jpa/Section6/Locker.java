package jpa.Section6;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    //양방향을 만들고 싶다면
    //읽기 전용
    @OneToOne(mappedBy = "locker")
    private Member member;

}
