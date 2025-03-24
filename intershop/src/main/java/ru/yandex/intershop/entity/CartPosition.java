package ru.yandex.intershop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart_positions")
@Getter
@Setter
@ToString
public class CartPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "item_price", nullable = false)
    private Integer itemPrice;
}
