package com.ken.redistemplatesample;

import com.ken.redistemplatesample.model.Book;
import com.ken.redistemplatesample.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

@SpringBootApplication
@Slf4j
public class RedisTemplateSampleApplication  implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(RedisTemplateSampleApplication.class, args);
    }

    @Autowired
    private BookService bookService;

    @Bean
    public RedisTemplate<String, Book> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Book> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Book> bookOptional = bookService.findOneBook("python");
        Optional<Book> bookOptional2 = bookService.findOneBook("python");

        bookService.getBookString("python");

        bookService.getBookString("python");



    }
}
