package ru.yandex.intershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IntershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntershopApplication.class, args);
	}

}
