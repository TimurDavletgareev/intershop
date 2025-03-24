package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.repository.ItemRepository;

import java.util.Optional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ItemEntityService {

    private final ItemRepository itemRepository;

    public Page<Item> findAll(Pageable pageable) {
        log.info("Finding all items");
        Page<Item> items = itemRepository.findAll(pageable);
        log.info("Found {} items", items.getTotalElements());
        return items;
    }

    public Item findById(Long id) {
        log.info("Find Item by id: {}", id);
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            log.info("Item by id={} found successfully", id);
            return item.get();
        }
        log.info("Item by id={} not found", id);
        return null;
    }

    public List<Item> findByIdIn(List<Long> ids) {
        log.info("Find Items by ids");
        List<Item> items = itemRepository.findByIdIn(ids);
        if (!items.isEmpty()) {
            log.info("Items by ids found successfully, ids list size={}", ids.size());
        } else {
            log.info("Item by ids not found, ids list size={}", ids.size());
        }
        return items;
    }

    public Page<Item> findByTitle(String title, Pageable pageable) {
        log.info("Finding items by title: {}", title);
        Page<Item> items = itemRepository.findByTitleContainsIgnoreCase(title, pageable);
        log.info("Found {} items by title: {}", items.getTotalElements(), title);
        return items;
    }

    public Page<Item> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable) {
        log.info("Finding items by price between: {} - {}", minPrice, maxPrice);
        Page<Item> items = itemRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        log.info("Found {} items by by price between: {} - {}", items.getTotalElements(), minPrice, maxPrice);
        return items;
    }

    public Integer findMaxPrice() {
        log.info("Finding max price");
        return itemRepository.findMaxPrice();
    }
}
