package spring.lectureA.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA.domain.item.Item;
import spring.lectureA.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 상품 등록
     * @param item
     */
    @Transactional //이 메소드는 저장을 해야하니까 Transactional Override
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 상품 단건 조회
     * @param itemId
     * @return
     */
    public Item findItem(Long itemId) {
        return itemRepository.findOneItem(itemId);
    }

    /**
     * 상품 전체 조회
     * @return
     */
    public List<Item> findItems() {
        return itemRepository.findAllItems();
    }
}
