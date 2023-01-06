package com.ken.bookshop.controller;

import com.ken.bookshop.model.Book;
import com.ken.bookshop.service.BookService;
import com.ken.bookshop.support.BookProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;


    @GetMapping(path = "/getAll", params = "!name")
    public List<Book> getAll() {
        return bookService.getAllBook();
    }

    @Autowired
    private BookProperties bookProperties;
    @GetMapping(path = "/getDiscount")
    public Integer getDiscount() {
        return bookProperties.getDiscount();
    }

    @GetMapping("/{id}")
    public Optional<Book> getById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBook(id);

        return book;
    }

    @GetMapping(path = "/", params = "name")
    public Optional<Book> getByName(@RequestParam String name) {
        return bookService.getBook(name);
    }
}
