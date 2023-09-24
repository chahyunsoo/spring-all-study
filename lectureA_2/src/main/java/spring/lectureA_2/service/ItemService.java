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

//    @Autowired private final ItemRepository itemRepository;
    private final ItemRepositoryWithSpringDataJpa itemRepositoryWithSpringDataJpa;

    @Transactional
    public void saveItem(Item item) {
        itemRepositoryWithSpringDataJpa.save(item);
    }

    @Transactional
    public void updateItem(Long itemId,String name,int price,int stockQuantity) {
        Optional<Item> findItem = itemRepositoryWithSpringDataJpa.findOne(itemId);

        findItem.get().setName(name);
        findItem.get().setPrice(price);
        findItem.get().setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepositoryWithSpringDataJpa.findAll();
    }
    public Optional<Item> findOne(Long itemId) {
        return itemRepositoryWithSpringDataJpa.findOne(itemId);
    }
}

