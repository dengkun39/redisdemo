package com.ken.redistemplatesample.repository;

import com.ken.redistemplatesample.model.Book;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {
    Map<String, Book> bookMap = new HashMap<>();
    public BookRepository(){
        bookMap.put("apache kafka", Book.builder()
                .name("apache kafka").id(1L).author("zhangsan")
                .build());
        bookMap.put("python", Book.builder()
                .name("python").id(2L).author("lisi")
                .build());
    }

    public Optional<Book> getBook(String name){
        if(bookMap.containsKey(name)){
            return Optional.of(bookMap.get(name));
        }
        else{
            return Optional.empty();
        }
    }
}
