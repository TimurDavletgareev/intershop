package ru.yandex.intershop.payment_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.api.PaymentControllerApi;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentControllerApi paymentControllerApi;

    public Mono<Integer> getBalance(Long userId) {
        log.info("Returning balance for user {}", userId);
        Mono<Integer> balance = paymentControllerApi.balance(userId);
        log.info("Balance for user {} returned: {}", userId, balance);
        return balance;
    }

    public Mono<Void> makePayment(Long userId, Integer amount) {
        log.info("Making payment for user {} with amount {}", userId, amount);
        return paymentControllerApi.pay(userId, amount).then();
    }
}
