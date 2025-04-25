package ru.yandex.intershop.service.entity;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.intershop.config.EmbeddedRedisConfiguration;
import ru.yandex.intershop.util.PageRequestCreator;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(EmbeddedRedisConfiguration.class)
@ActiveProfiles("test")
class ItemEntityServiceTest {

    @Autowired
    private ItemEntityService itemEntityService;

    @Test
    void shouldFindById() throws InterruptedException {

        String result;
        try (
                RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
                StatefulRedisConnection<String, String> connection = redisClient.connect()
        ) {
            result = connection.sync().get("item::1");

            System.out.println("Initial result: " + result);
            assertNull(result);

            System.out.println("call first time:");
            itemEntityService.findById(1L).block();
            System.out.println("End of call first time");

            result = connection.sync().get("item::1");

            System.out.println("result after first call: " + result);
            assertNotNull(result);

            System.out.println("call second time:");
            itemEntityService.findById(1L).block();
            System.out.println("End of call second time");

            TimeUnit.SECONDS.sleep(6L);

            result = connection.sync().get("item::1");
            System.out.println("result after sleep: " + result);
            assertNull(result);

            System.out.println("call third time:");
            itemEntityService.findById(1L).block();
            System.out.println("End of call third time");
        }
    }

    @Test
    void shouldFindAll() throws InterruptedException {

        String result;
        try (
                RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
                StatefulRedisConnection<String, String> connection = redisClient.connect()
        ) {
            Sort sort = Sort.by("id");
            Pageable pageable = PageRequestCreator.create(0, 5, sort);

            result = connection.sync().get("items::" + pageable);

            System.out.println("Initial result: " + result);
            assertNull(result);

            System.out.println("call first time:");
            itemEntityService.findAll(pageable).collectList().block();
            System.out.println("End of call first time");

            result = connection.sync().get("items::" + pageable);

            System.out.println("result after first call: " + result);
            assertNotNull(result);

            System.out.println("call second time:");
            itemEntityService.findAll(pageable).collectList().block();
            System.out.println("End of call second time");

            TimeUnit.SECONDS.sleep(6L);

            result = connection.sync().get("items::" + pageable);
            System.out.println("result after sleep: " + result);
            assertNull(result);

            System.out.println("call third time:");
            itemEntityService.findAll(pageable).collectList().block();
            System.out.println("End of call third time");
        }
    }
}