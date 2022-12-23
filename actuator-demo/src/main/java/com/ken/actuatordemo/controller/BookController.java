package com.ken.actuatordemo.controller;

import com.ken.actuatordemo.model.Book;
import com.ken.actuatordemo.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody Book newBook) {
        bookService.createBook(newBook);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        Book book = bookService.getBook();
        log.info("Book {}:", book);
        return book;
    }

}
