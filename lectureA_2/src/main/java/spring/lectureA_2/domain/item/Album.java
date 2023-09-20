package spring.lectureA_2.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter @Setter
public class Album extends Item{
    @Column(name = "artist")
    private String artist;

    @Column(name = "etc")
    private String etc;

    public String getItemType() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
