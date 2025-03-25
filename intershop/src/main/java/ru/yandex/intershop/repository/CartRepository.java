package ru.yandex.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.CartPosition;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartPosition, Long> {

    List<CartPosition> findByUserId(Long userId);

    Optional<CartPosition> findByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserId(Long userId);
}
