package spring.lectureA_2.controller;

import lombok.Getter;
import lombok.Setter;
import spring.lectureA_2.domain.item.Album;

@Getter
@Setter
public class AlbumForm {

    //공통 속성
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    //개별 속성
    private String artist;
    private String etc;

}
