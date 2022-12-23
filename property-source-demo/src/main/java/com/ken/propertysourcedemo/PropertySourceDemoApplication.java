package com.ken.propertysourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PropertySourceDemoApplication implements ApplicationRunner {
    @Value("${property.demo.test}")
    private String hello;

    @Value("${property.demo.random}")
    private int random_value;
    public static void main(String[] args) {
        SpringApplication.run(PropertySourceDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("{} {}", hello, random_value);
    }
}
