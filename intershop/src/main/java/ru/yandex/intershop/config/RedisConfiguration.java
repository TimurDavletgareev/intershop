package ru.yandex.intershop.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.yandex.intershop.entity.Item;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCacheManagerBuilderCustomizer itemsCacheCustomizer() {
        return builder -> builder.withCacheConfiguration(
                "items",                                         // Имя кеша
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.of(5, ChronoUnit.SECONDS))  // TTL
                        .serializeValuesWith(                          // Сериализация JSON
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(Item.class)
                                )
                        )
        );
    }
}
