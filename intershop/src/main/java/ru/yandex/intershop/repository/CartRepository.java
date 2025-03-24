package ru.yandex.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
