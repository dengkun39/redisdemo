package com.ken.rediscachesample.service;

import com.ken.rediscachesample.model.CacheBook;
import com.ken.rediscachesample.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
@CacheConfig(cacheNames = "cache-book")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Cacheable
    public List<CacheBook> findAllCoffee() {
        return bookRepository.findAll();
    }

    @CacheEvict
    public void reloadCoffee() {
    }

    public Optional<CacheBook> findOneBook(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<CacheBook> coffee = bookRepository.findOne(
                Example.of(CacheBook.builder().name(name).build(), matcher));
        log.info("Book Found: {}", coffee);
        return coffee;
    }
}
