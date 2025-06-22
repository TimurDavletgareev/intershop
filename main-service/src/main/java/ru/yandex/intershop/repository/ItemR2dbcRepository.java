package ru.yandex.intershop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.yandex.intershop.entity.Item;

import java.util.List;

@Repository
public interface ItemR2dbcRepository extends R2dbcRepository<Item, Long> {

    Flux<Item> findByAmountInStockGreaterThan(Integer minAmount, Pageable pageable); //findAll

    Flux<Item> findByTitleContainsIgnoreCase(String title, Pageable pageable);

    Flux<Item> findByIdIn(List<Long> ids);
}
