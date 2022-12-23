package com.ken.bookshop.repository;

import com.ken.bookshop.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "rest-books", path = "rest-books")
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByName(@Param("name") String name);
}

