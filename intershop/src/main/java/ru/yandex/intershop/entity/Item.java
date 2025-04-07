package ru.yandex.intershop.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;


@Table(name = "items")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @NonNull
    private String title;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @Column
    @NonNull
    private Integer amountInStock;

    @Column
    @NonNull
    private Integer price;
}
