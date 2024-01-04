package spring.lecture0.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
@Entity  //JPA가 관리하는
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 id를 자동으로 생성해주는 것
    private Long id;

//    @Column(name = "username")  //테이블의 컬럼명을 지정함
    private String name;

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


}
