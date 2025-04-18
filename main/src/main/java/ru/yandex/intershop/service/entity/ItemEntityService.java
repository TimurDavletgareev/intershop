package ru.yandex.intershop.service.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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

    //@Cacheable(value = "items", key = "#pageable")
    public Flux<Item> findAll(Pageable pageable) {
        log.info("Finding all items");
        return itemR2dbcRepository.findByAmountInStockGreaterThan(0, pageable);
    }

    @Cacheable(value = "items", key = "#id")
    public Mono<Item> findById(Long id) {
        log.info("Find Item by id: {}", id);
        return itemR2dbcRepository.findById(id)
                .doOnNext(item -> log.info("Item by id={} found: {}", id, item));
    }

    //@Cacheable(value = "items", key = "#ids")
    public Flux<Item> findByIdIn(List<Long> ids) {
        log.info("Find Items by ids");
        return itemR2dbcRepository.findByIdIn(ids);
    }

    //@Cacheable(value = "items", key = "#title")
    public Flux<Item> findByTitle(String title, Pageable pageable) {
        log.info("Finding items by title: {}", title);
        return itemR2dbcRepository.findByTitleContainsIgnoreCase(title, pageable);
    }

    @Cacheable(value = "items", key = "'count'")
    public Mono<Long> count() {
        return itemR2dbcRepository.count();
    }
}
