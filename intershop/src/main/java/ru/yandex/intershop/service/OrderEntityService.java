package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.Order;
import ru.yandex.intershop.repository.OrderR2dbcRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderEntityService {

    private final OrderR2dbcRepository orderR2dbcRepository;

    public Order findById(Long id) {
        log.info("Find order by id: {}", id);
        Optional<Order> order = orderR2dbcRepository.findById(id);
        if (order.isPresent()) {
            log.info("Order by id={} found successfully", id);
            return order.get();
        }
        log.info("Order by id={} not found", id);
        return null;
    }

    public List<Order> findByUserId(Long userId) {
        log.info("Find orders by userId: {}", userId);
        List<Order> orders = orderR2dbcRepository.findByUserId(userId);
        if (!orders.isEmpty()) {
            log.info("Orders by userId={} found successfully", userId);
        } else {
            log.info("Orders by userId={} not found", userId);
        }
        return orders;
    }

    public List<Order> findByOrderUid(String uid) {
        log.info("Find order by uid: {}", uid);
        List<Order> order = orderR2dbcRepository.findByOrderUid(uid);
        if (!order.isEmpty()) {
            log.info("Order by uid={} found successfully", uid);
        } else {
            log.info("Order by uid={} not found", uid);
        }
        return order;
    }

    public Order save(Order order) {
        log.info("Save order: {}", order);
        Order savedOrder = orderR2dbcRepository.save(order);
        log.info("Order saved: {}", savedOrder);
        return savedOrder;
    }

    public void saveAll(List<Order> orders) {
        log.info("Save {} orders", orders.size());
        orderR2dbcRepository.saveAll(orders);
        log.info("Orders saved");
    }

    public void delete(Order order) {
        log.info("Delete order: {}", order);
        orderR2dbcRepository.delete(order);
        log.info("Order deleted: {}", order);
    }
}
