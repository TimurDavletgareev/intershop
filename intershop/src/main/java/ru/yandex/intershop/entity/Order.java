package ru.yandex.intershop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "order_uid", nullable = false)
    private String orderUid;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "item_price", nullable = false)
    private Integer itemPrice;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;
}
