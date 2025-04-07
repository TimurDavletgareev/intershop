package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.repository.ItemR2dbcRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ItemEntityService {

    private final ItemR2dbcRepository itemR2dbcRepository;

    public Flux<Item> findAll(Pageable pageable) {
        log.info("Finding all items");
        return itemR2dbcRepository.findByAmountInStockGreaterThan(0, pageable);
    }

    public Mono<Item> findById(Long id) {
        log.info("Find Item by id: {}", id);
        return itemR2dbcRepository.findById(id)
                .doOnNext(item -> log.info("Item by id={} found: {}", id, item));
    }

    public Flux<Item> findByIdIn(List<Long> ids) {
        log.info("Find Items by ids");
        return itemR2dbcRepository.findByIdIn(ids);
    }

    public Flux<Item> findByTitle(String title, Pageable pageable) {
        log.info("Finding items by title: {}", title);
        return itemR2dbcRepository.findByTitleContainsIgnoreCase(title, pageable);
    }

    public Flux<Item> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable) {
        log.info("Finding items by price between: {} - {}", minPrice, maxPrice);
        return itemR2dbcRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Mono<Integer> findMaxPrice() {
        log.info("Finding max price");
        return itemR2dbcRepository.findMaxPrice();
    }

    public Mono<Long> count() {
        return itemR2dbcRepository.count();
    }
}
