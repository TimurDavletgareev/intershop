package ru.yandex.intershop.service.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.CartPosition;
import ru.yandex.intershop.repository.CartR2dbcRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CartEntityService {

    private final CartR2dbcRepository cartR2dbcRepository;

    public Mono<CartPosition> findByUserIdAndItemId(Long userId, Long itemId) {
        log.info("Find CartPosition by userId={} and itemId={}", userId, itemId);
        return cartR2dbcRepository.findByUserIdAndItemId(userId, itemId)
                .doOnNext(cartPosition -> log.info("Found CartPosition by userId={} adn itemId={} : {}",
                        userId, itemId, cartPosition));
    }

    @Cacheable(value = "cart", key = "#userId")
    public Mono<List<CartPosition>> findByUserId(Long userId) {
        log.info("Find CartPositions by userId: {}", userId);
        return cartR2dbcRepository.findByUserId(userId)
                .collectList();
    }

    public Mono<Void> deleteById(Long cartPositionId) {
        log.info("Deleting CartPosition by id: {}", cartPositionId);
        return cartR2dbcRepository.deleteById(cartPositionId)
                .doOnSuccess(it -> log.info("CartPosition deleted by id={}", cartPositionId))
                .doOnError(throwable ->
                        log.info("CartPosition by id={} NOT deleted: unable to find cartPositionId", cartPositionId))
                .then();
    }

    public Mono<Void> save(CartPosition cartPosition) {
        log.info("Save CartPosition: {}", cartPosition);
        return cartR2dbcRepository.save(cartPosition)
                .doOnNext(it -> log.info("Saved CartPosition: {}", it))
                .then();
    }

    public Mono<Void> deleteByUserId(Long userId) {
        log.info("Deleting CartPositions by userId: {}", userId);
        return cartR2dbcRepository.deleteByUserId(userId)
                .doOnSuccess(it -> log.info("All CartPositions deleted by userId={}", userId))
                .doOnError(throwable ->
                        log.info("CartPositions by userId={} NOT deleted", userId))
                .then();
    }
}
