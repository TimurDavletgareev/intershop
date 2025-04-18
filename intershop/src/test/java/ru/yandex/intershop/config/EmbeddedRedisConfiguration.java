package ru.yandex.intershop.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * Конфигурация для тестов, использующих embedded-redis
 */
@TestConfiguration
public class EmbeddedRedisConfiguration {

    /**
     * Создаём в контексте бин RedisServer
     */
    @Bean(destroyMethod = "stop") // Останавливаем сервер при закрытии контекста
    public RedisServer redisServer() throws IOException {
        var redisServer = new RedisServer();
        redisServer.start(); // Запускаем прямо во время инициализации бина
        return redisServer;
    }

}
