package com.ken.redisrepositorysample.service;

import com.ken.redisrepositorysample.model.CacheBook;
import com.ken.redisrepositorysample.repository.BookRepository;
import com.ken.redisrepositorysample.repository.CacheBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookService {
    private static final String CACHE = "repository-book";
    @Autowired
    private CacheBookRepository cacheRepository;

    @Autowired
    private BookRepository bookRepository;

    public Optional<CacheBook> findOneBook(String name) {
        Optional<CacheBook> optionalCacheBook = cacheRepository.findOneByName(name);
        if(!optionalCacheBook.isPresent())
        {
            Optional<CacheBook> book = bookRepository.getBook(name);
            log.info("Book Found: {}", book);
            if (book.isPresent()) {
                log.info("Put book {} to Redis.", name);
                cacheRepository.save(book.get());
            }
            return book;
        }
        return optionalCacheBook;
    }

    public Optional<CacheBook> findOneBookByAuthor(String name) {
        Optional<CacheBook> optionalCacheBook = cacheRepository.findOneByAuthor(name);
        return optionalCacheBook;
    }
}
