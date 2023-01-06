package com.ken.resilience4j.bookcustomer.controller;

import com.ken.resilience4j.bookcustomer.integration.BookService;
import com.ken.resilience4j.bookcustomer.model.Book;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/customer")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    private CircuitBreaker circuitBreaker;

    public BookController(CircuitBreakerRegistry registry) {
        circuitBreaker = registry.circuitBreaker("menu");
    }

    @GetMapping("/menu")
    public List<Book> readMenu() {
        Supplier<List<Book>> supplier = () -> bookService.getAll();
        circuitBreaker.getEventPublisher()
                .onEvent(event -> log.info(event.toString()));
        try{
            return circuitBreaker.executeSupplier(supplier);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            return Collections.emptyList();
        }
    }
}
