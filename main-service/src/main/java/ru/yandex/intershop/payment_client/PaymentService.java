package ru.yandex.intershop.payment_client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.api.PaymentControllerApi;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentControllerApi paymentControllerApi;

    public Mono<Integer> getBalance(Long userId) {
        return paymentControllerApi.balance(userId);
    }

    public Mono<Integer> makePayment(Long userId, Integer amount) {
        return paymentControllerApi.pay(userId, amount);
    }
}
