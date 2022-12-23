package com.ken.bookshop.service;

import com.ken.bookshop.model.Book;
import com.ken.bookshop.repository.BookRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@Slf4j
public class BookService implements MeterBinder {
    @Autowired
    private BookRepository bookRepository;
    private Counter orderCounter = null;

    public Long createBook(Book book){
        Book storeBook = bookRepository.save(book);
        if(storeBook.getId() > 0)
        {
            orderCounter.increment();
        }

        return storeBook.getId();

    }

    public List<Book> getAllBook() {
        return bookRepository.findAll(Sort.by("id"));
    }

    public Optional<Book> getBook(String name){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Book> book = bookRepository.findOne(
                Example.of(Book.builder().name(name).build(), matcher));
        return book;
    }

    public Optional<Book> getBook(Long id){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", exact().ignoreCase());
        Optional<Book> book = bookRepository.findOne(
                Example.of(Book.builder().id(id).build(), matcher));
        return book;
    }



    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        orderCounter = meterRegistry.counter("book.count");
    }
}
