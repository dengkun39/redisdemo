package com.ken.flinklearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlinkApplication.class, args);
    }

}
