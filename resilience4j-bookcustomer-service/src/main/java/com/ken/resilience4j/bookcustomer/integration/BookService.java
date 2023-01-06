package com.ken.resilience4j.bookcustomer.integration;

import com.ken.resilience4j.bookcustomer.model.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "bookshop-service", contextId = "book", path = "/book")
public interface BookService {
    @GetMapping(path = "/getAll", params = "!name")
    List<Book> getAll();

    @GetMapping("/{id}")
    Optional<Book> getById(@PathVariable("id") Long id);


}
