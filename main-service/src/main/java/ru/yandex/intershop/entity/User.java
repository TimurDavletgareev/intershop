package ru.yandex.intershop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @NonNull
    private String username;

    @Column
    @NonNull
    private String email;

    @Column
    private LocalDate birthDate;

    @Column
    private LocalDateTime regDate;

    @JsonIgnore
    @ToString.Exclude
    private String password;
}
