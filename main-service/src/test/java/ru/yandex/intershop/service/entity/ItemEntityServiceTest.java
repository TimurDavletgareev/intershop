package ru.yandex.intershop.service.entity;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.intershop.config.EmbeddedRedisConfiguration;
import ru.yandex.intershop.entity.Item;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Import(EmbeddedRedisConfiguration.class)
@ActiveProfiles("test")
@Disabled
class ItemEntityServiceTest {

    @Autowired
    private ItemEntityService itemEntityService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findById() throws InterruptedException {

        String result;
        try (
                RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
                StatefulRedisConnection<String, String> connection = redisClient.connect()
        ) {
            result = connection.sync().get("items::1");

            System.out.println("Initial result: " + result);
            assertNull(result);

            System.out.println("call first time:");
            Item item = itemEntityService.findById(1L).block();
            System.out.println("End of call first time");

            result = connection.sync().get("items::1");

            System.out.println("result after first call: " + result);
            assertNotNull(result);

            System.out.println("call second time:");
            item = itemEntityService.findById(1L).block();
            System.out.println("End of call second time");

            TimeUnit.SECONDS.sleep(6L);

            result = connection.sync().get("items::1");
            System.out.println("result after sleep: " + result);
            assertNull(result);

            System.out.println("call third time:");
            item = itemEntityService.findById(1L).block();
            System.out.println("End of call third time");
        }
    }
}