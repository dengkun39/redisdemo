package com.ken.redisrepositorysample;

import com.ken.redisrepositorysample.model.CacheBook;
import com.ken.redisrepositorysample.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class RedisCacheDemoApplication implements ApplicationRunner {
	@Autowired
	private BookService bookService;
	public static void main(String[] args) {
		SpringApplication.run(RedisCacheDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Optional<CacheBook> bookOptional = bookService.findOneBook("python");
		Optional<CacheBook> bookOptional2 = bookService.findOneBook("apache kafka");
		//Optional<CacheBook> bookOptional2 = bookService.findOneBookByAuthor("lisi");
	}
}
