package com.ken.actuatordemo.service;

import com.ken.actuatordemo.model.Book;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookService implements MeterBinder {
    private Counter orderCounter = null;

    public void createBook(Book book){
        orderCounter.increment();
    }

    public Book getBook(){
        return Book.builder()
                .id(1L)
                .author("zhangsan")
                .name("java")
                .build();
    }


    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        orderCounter = meterRegistry.counter("book.count");
    }
}
