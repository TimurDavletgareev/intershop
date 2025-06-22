package ru.yandex.intershop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.intershop.api.PaymentControllerApi;
import ru.yandex.intershop.client.ApiClient;

@Configuration
public class PaymentClientConfig {

    @Value("${intershop-payment.url}")
    private String paymentUrl;

    @Bean
    public PaymentControllerApi paymentControllerApi() {
        return new PaymentControllerApi(apiClient());
    }

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(paymentUrl);
        return apiClient;
    }
}
