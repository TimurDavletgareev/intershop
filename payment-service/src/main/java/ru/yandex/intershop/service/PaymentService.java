package ru.yandex.intershop.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.error.exception.ConflictOnRequestException;

@Service
@Slf4j
@NoArgsConstructor
public class PaymentService {

    private Integer balance = 20000;

    public Integer getBalanceByUserId(Long userId) {
        log.info("getBalanceByUserId={}", userId);
        Integer balance = this.balance;
        log.info("BalanceByUserId={}: {}", userId, balance);
        return balance;
    }

    public Integer makePayment(Long userId, Integer amount) {
        log.info("Making Payment for userId={} with amount={}", userId, amount);
        this.balance -= amount;
        if (this.balance < 0) {
            throw new ConflictOnRequestException("Balance is negative");
        }
        log.info("Balance By UserId={} after making payment with amount={}: {}", userId, amount, this.balance);
        return balance;
    }
}
