package spring.lectureA_2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.lectureA_2.domain.item.Item;
import spring.lectureA_2.repository.ItemRepository;
import spring.lectureA_2.repository.ItemRepositoryWithSpringDataJpa;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    @Autowired private final ItemRepository itemRepository;
//    private final ItemRepositoryWithSpringDataJpa itemRepositoryWithSpringDataJpa;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId,String name,int price,int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}

