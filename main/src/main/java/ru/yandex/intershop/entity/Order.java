package ru.yandex.intershop.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @NonNull
    private String orderUid;

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

    @Column
    @NonNull
    private Integer totalPrice;
}
