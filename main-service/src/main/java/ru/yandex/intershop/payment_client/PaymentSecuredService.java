package ru.yandex.intershop.payment_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentSecuredService {

    @Value("${intershop-payment.url}")
    private String paymentUrl;

    private final ReactiveOAuth2AuthorizedClientManager manager;

    private final WebClient webClient = WebClient.create(paymentUrl);

    public Mono<Integer> getBalance(Long userId) {
        log.info("Returning balance for user {}", userId);
        String endpoint = "/payment/balance/" + userId;
        return manager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("intershop")
                        .principal("system")
                        .build()) // Mono<OAuth2AuthorizedClient>
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(accessToken -> webClient.get()
                        .uri(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(Integer.class)
                        .block()
                )
                .map(balance -> {
                    log.info("Balance for user {} returned: {}", userId, balance);
                    return balance;
                });
    }

    public Mono<Void> makePayment(Long userId, Integer amount) {
        log.info("Making payment for user {} with amount {}", userId, amount);
        String endpoint = String.format("/payment/pay/%d/%d", userId, amount);
        return manager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("intershop")
                        .principal("system")
                        .build()) // Mono<OAuth2AuthorizedClient>
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue)
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(accessToken -> webClient.post()
                        .uri(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .toBodilessEntity()
                )
                .then();
    }
}
