package ru.yandex.intershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/", "/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .formLogin(withDefaults())
                /* Вход через OAuth 2.0 провайдеров
                 .oauth2Login()
                */
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) ->
                                exchange.getExchange().getSession()
                                        .flatMap(WebSession::invalidate) // удаляем сессию
                                        .then(Mono.fromRunnable(() -> {
                                            exchange.getExchange().getResponse()
                                                    .setStatusCode(HttpStatus.OK); // отвечаем 200 OK
                                        }))
                        )
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler((exchange, denied) ->
                                Mono.error(new AccessDeniedException("Access Denied")))
                )
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
