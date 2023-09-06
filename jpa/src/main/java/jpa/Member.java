package jpa;

import javax.persistence.*;


//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 10)
public class Member {
    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB에 값이 들어가봐야 id값을 알 수가 있다.
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    @Column(name = "name",nullable = false)
    private String username;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

