package ru.yandex.intershop.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table(name = "cart_positions")
@Getter
@Setter
@ToString
public class CartPosition {

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @NonNull
    private Long userId;

    @Column
    @NonNull
    private Long itemId;

    @Column
    @NonNull
    private Integer amount;

    @Column
    @NonNull
    private Integer itemPrice;
}
