package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.Cart;
import ru.yandex.intershop.repository.CartRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CartEntityService {

    private final CartRepository cartRepository;

    public Cart findById(Long id) {
        log.info("Find Cart by id: {}", id);
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            log.info("Cart by id={} found successfully", id);
            return cart.get();
        }
        log.info("Cart by id={} not found", id);
        return null;
    }

    public Cart findByUserId(Long userId) {
        log.info("Find Cart by userId: {}", userId);
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isPresent()) {
            log.info("Cart by userId={} found successfully", userId);
            return cart.get();
        }
        log.info("Cart by userId={} not found", userId);
        return null;
    }
}
