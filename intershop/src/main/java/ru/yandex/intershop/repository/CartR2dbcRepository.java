package ru.yandex.intershop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.CartPosition;

@Repository
public interface CartR2dbcRepository extends ReactiveCrudRepository<CartPosition, Long> {

    Flux<CartPosition> findByUserId(Long userId);

    Mono<CartPosition> findByUserIdAndItemId(Long userId, Long itemId);

    Mono<Void> deleteByUserId(Long userId);
}
