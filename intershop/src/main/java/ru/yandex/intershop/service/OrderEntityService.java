package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.Order;
import ru.yandex.intershop.repository.OrderRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderEntityService {

    private final OrderRepository orderRepository;

    public Order findById(Long id) {
        log.info("Find order by id: {}", id);
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            log.info("Order by id={} found successfully", id);
            return order.get();
        }
        log.info("Order by id={} not found", id);
        return null;
    }

    public Order findByOrderUid(String orderUid) {
        log.info("Find order by orderUid: {}", orderUid);
        Optional<Order> order = orderRepository.findByOrderUid(orderUid);
        if (order.isPresent()) {
            log.info("Order by orderUid={} found successfully", orderUid);
            return order.get();
        }
        log.info("Order by orderUid={} not found", orderUid);
        return null;
    }

    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        log.info("Find orders by userId: {}", userId);
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        if (!orders.isEmpty()) {
            log.info("Orders by userId={} found successfully", userId);
        } else {
            log.info("Orders by userId={} not found", userId);
        }
        return orders;
    }

    public Order save(Order order) {
        log.info("Save order: {}", order);
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved: {}", savedOrder);
        return savedOrder;
    }

    public void delete(Order order) {
        log.info("Delete order: {}", order);
        orderRepository.delete(order);
        log.info("Order deleted: {}", order);
    }
}
