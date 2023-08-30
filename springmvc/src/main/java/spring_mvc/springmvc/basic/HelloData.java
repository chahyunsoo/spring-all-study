package spring_mvc.springmvc.basic;

import lombok.Data;

@Data //@Getter @Setter @EqualsAndHashCode @RequiredArgsConstructor 를 모두 자동으로 적용해주는 어노테이션인 @Data
public class HelloData {
    private String username;
    private int age;
}
