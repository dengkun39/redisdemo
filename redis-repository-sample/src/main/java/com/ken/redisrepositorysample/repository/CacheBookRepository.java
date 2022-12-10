package com.ken.redisrepositorysample.repository;

import com.ken.redisrepositorysample.model.CacheBook;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CacheBookRepository extends CrudRepository<CacheBook, Long> {
    Optional<CacheBook> findOneByName(String name);
    Optional<CacheBook> findOneByAuthor(String author);
}

