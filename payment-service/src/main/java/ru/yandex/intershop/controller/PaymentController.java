package ru.yandex.intershop.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.api.PaymentApi;
import ru.yandex.intershop.service.PaymentService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/payment/balance/{userId}",
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<Integer>> balance(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        ResponseEntity<Integer> resp = ResponseEntity.status(OK).body(paymentService.getBalanceByUserId(userId));
        return Mono.just(resp);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/payment/pay/{userId}/{amount}",
            produces = {"application/json"}
    )
    public Mono<ResponseEntity<Integer>> pay(
            @Parameter(name = "userId", description = "", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "amount", description = "", required = true, in = ParameterIn.PATH) @PathVariable("amount") Integer amount,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        ResponseEntity<Integer> resp = ResponseEntity.status(OK).body(paymentService.makePayment(userId, amount));
        return Mono.just(resp);
    }
}
