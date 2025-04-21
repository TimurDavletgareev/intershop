package ru.yandex.intershop.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
public class PaymentService {

    private Integer balance = 20000;

    public Integer getBalanceByUserId(Long userId) {
        return balance;
    }

    public Integer makePayment(Long userId, Integer amount) {
        this.balance -= amount;
        return balance;
    }
}
