package com.ken.rediscachesample.repository;

import com.ken.rediscachesample.model.CacheBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<CacheBook, Long> {
}
