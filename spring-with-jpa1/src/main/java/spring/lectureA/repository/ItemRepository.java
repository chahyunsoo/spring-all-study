package spring.lectureA.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.lectureA.domain.Member;
import spring.lectureA.domain.item.Item;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 상품 저장
     * @param item
     */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        }
    }

    /**
     * 상품 단건 조회
     * @param id
     * @return
     */
    public Item findOneItem(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 상품 전체 조회
     * @return
     */
    public List<Item> findAllItems() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }












}
