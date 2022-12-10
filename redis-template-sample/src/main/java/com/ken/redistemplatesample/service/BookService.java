package com.ken.redistemplatesample.service;

import com.ken.redistemplatesample.model.Book;
import com.ken.redistemplatesample.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class BookService {
    private static final String CACHE = "book";
    private static final String STRINGCACHE = "string_book";
    @Autowired
    private RedisTemplate<String, Book> redisTemplate;

    @Autowired
    private BookRepository bookRepository;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    public Optional<Book> findOneBook(String name) {
        HashOperations<String, String, Book> hashOperations = redisTemplate.opsForHash();
        if (redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE, name)) {
            log.info("Get book {} from Redis.", name);
            return Optional.of(hashOperations.get(CACHE, name));
        }

        Optional<Book> book = bookRepository.getBook(name);
        log.info("Book Found: {}", book);
        if (book.isPresent()) {
            log.info("Put book {} to Redis.", name);
            hashOperations.put(CACHE, name, book.get());
            redisTemplate.expire(CACHE, 10, TimeUnit.MINUTES);
        }
        return book;
    }

    public Optional<String> getBookString(String name){
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        if (stringRedisTemplate.hasKey(STRINGCACHE) && hashOperations.hasKey(STRINGCACHE, name)) {
            log.info("Get book {} from Redis.", name);
            return Optional.of(hashOperations.get(STRINGCACHE, name));
        }

        Optional<Book> book = bookRepository.getBook(name);
        log.info("Book Found: {}", book);
        if (book.isPresent()) {
            log.info("Put book {} to Redis.", name);
            hashOperations.put(STRINGCACHE, name, book.get().getAuthor());
            stringRedisTemplate.expire(STRINGCACHE, 10, TimeUnit.MINUTES);
            return Optional.of(book.get().getAuthor());
        }
        return Optional.empty();
    }



}
