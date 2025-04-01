package ru.yandex.intershop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.Item;

import java.util.List;

@Repository
public interface ItemR2dbcRepository extends ReactiveCrudRepository<Item, Long> {

    Flux<Item> findByTitleContainsIgnoreCase(String title, Pageable pageable);

    Flux<Item> findByPriceBetween(Integer priceAfter, Integer priceBefore, Pageable pageable);

    Flux<Item> findByIdIn(List<Long> ids);

    @Query("SELECT MAX(i.price) FROM Item i")
    Mono<Integer> findMaxPrice();
}
