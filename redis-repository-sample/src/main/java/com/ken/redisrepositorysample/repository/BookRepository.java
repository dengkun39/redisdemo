package com.ken.redisrepositorysample.repository;

import com.ken.redisrepositorysample.model.CacheBook;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {
    Map<String, CacheBook> bookMap = new HashMap<>();
    public BookRepository(){
        bookMap.put("apache kafka", CacheBook.builder()
                .name("apache kafka").userId(1L).author("zhangsan")
                .build());
        bookMap.put("python", CacheBook.builder()
                .name("python").userId(2L).author("lisi")
                .build());
    }

    public Optional<CacheBook> getBook(String name){
        if(bookMap.containsKey(name)){
            return Optional.of(bookMap.get(name));
        }
        else{
            return Optional.empty();
        }
    }
}