package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.CartPosition;
import ru.yandex.intershop.repository.CartR2dbcRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CartEntityService {

    private final CartR2dbcRepository cartR2dbcRepository;

    public CartPosition findById(Long id) {
        log.info("Find CartPosition by id: {}", id);
        Optional<CartPosition> cart = cartR2dbcRepository.findById(id);
        if (cart.isPresent()) {
            log.info("CartPosition by id={} found successfully", id);
            return cart.get();
        }
        log.info("CartPosition by id={} not found", id);
        return null;
    }

    public CartPosition findByUserIdAndItemId(Long userId, Long itemId) {
        log.info("Find CartPosition by userId={} and itemId={}", userId, itemId);
        Optional<CartPosition> cart = cartR2dbcRepository.findByUserIdAndItemId(userId, itemId);
        if (cart.isPresent()) {
            log.info("CartPosition by userId={} and itemId={} found successfully", userId, itemId);
            return cart.get();
        } else {
            log.info("CartPosition by userId={} and itemId={} not found", userId, itemId);
        }
        return null;
    }

    public List<CartPosition> findByUserId(Long userId) {
        log.info("Find CartPositions by userId: {}", userId);
        List<CartPosition> userCartContent = cartR2dbcRepository.findByUserId(userId);
        if (!userCartContent.isEmpty()) {
            log.info("CartPositions by userId={} found successfully, list size={}", userId, userCartContent.size());
        } else {
            log.info("CartPositions by userId={} not found", userId);
        }
        return userCartContent;
    }

    public void save(CartPosition cartPosition) {
        log.info("Save CartPosition: {}", cartPosition);
        CartPosition savedCartPosition = cartR2dbcRepository.save(cartPosition);
        log.info("CartPosition saved: {}", savedCartPosition);
    }

    public void delete(Long cartPositionId) {
        log.info("Deleting CartPosition by id: {}", cartPositionId);
        Optional<CartPosition> cartPosition = cartR2dbcRepository.findById(cartPositionId);
        if (cartPosition.isPresent()) {
            cartR2dbcRepository.delete(cartPosition.get());
            log.info("CartPosition deleted: {}", cartPosition.get());
        } else {
            log.info("CartPosition by id={} NOT deleted: unable to find cartPositionId", cartPositionId);
        }
    }

    public void deleteByUserId(Long userId) {
        log.info("Deleting CartPositions by userId: {}", userId);
        cartR2dbcRepository.deleteByUserId(userId);
        log.info("All CartPositions deleted by userId={}", userId);
    }
}
