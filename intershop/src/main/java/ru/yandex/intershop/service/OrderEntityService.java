package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.Order;
import ru.yandex.intershop.repository.OrderR2dbcRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderEntityService {

    private final OrderR2dbcRepository orderR2dbcRepository;

    public Mono<Order> findById(Long id) {
        log.info("Find order by id: {}", id);
        return orderR2dbcRepository.findById(id)
                .doOnNext(it -> log.info("Order by id={} found: {}", id, it));
    }

    public Flux<Order> findByUserId(Long userId) {
        log.info("Find orders by userId: {}", userId);
        return orderR2dbcRepository.findByUserId(userId);
    }

    public Flux<Order> findByOrderUid(String uid) {
        log.info("Find order by uid: {}", uid);
        return orderR2dbcRepository.findByOrderUid(uid);
    }

    public Mono<Order> save(Order order) {
        log.info("Save order: {}", order);
        return orderR2dbcRepository.save(order)
                .doOnNext(it -> {
                    log.info("Saved order: {}", it);
                });
    }

    public Mono<Void> saveAll(List<Order> orders) {
        log.info("Save {} orders", orders.size());
        return orderR2dbcRepository.saveAll(orders)
                .then();

    }

    public Mono<Void> delete(Order order) {
        log.info("Delete order: {}", order);
        return orderR2dbcRepository.delete(order);
    }
}
