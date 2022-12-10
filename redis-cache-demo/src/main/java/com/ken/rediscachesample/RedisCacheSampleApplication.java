package com.ken.rediscachesample;

import com.ken.rediscachesample.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@EnableCaching(proxyTargetClass = true)
public class RedisCacheSampleApplication implements ApplicationRunner {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheSampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Count: {}", bookService.findAllCoffee().size());
        for (int i = 0; i < 5; i++) {
            log.info("Reading from cache.");
            bookService.findAllCoffee();
        }
        Thread.sleep(5000);
        log.info("Reading after refresh.");
        bookService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));
    }
}
