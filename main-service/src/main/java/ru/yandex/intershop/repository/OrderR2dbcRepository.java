package ru.yandex.intershop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.Order;

@Repository
public interface OrderR2dbcRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByOrderUid(String orderUid);

    Flux<Order> findByUserId(Long userId);

    Mono<Void> deleteByOrderUid(String orderUid);
}
