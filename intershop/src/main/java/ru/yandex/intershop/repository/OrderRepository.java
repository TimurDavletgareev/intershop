package ru.yandex.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderUid(String orderUid);

    List<Order> findByUserId(Long userId);

    void deleteByOrderUid(String orderUid);
}
