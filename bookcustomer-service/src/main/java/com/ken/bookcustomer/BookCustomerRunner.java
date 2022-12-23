package com.ken.bookcustomer;

import com.ken.bookcustomer.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
@Slf4j
public class BookCustomerRunner implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        showServiceInstances();
        readBooks();

        queryBook(1L);
    }

    private void showServiceInstances() {
        log.info("DiscoveryClient: {}", discoveryClient.getClass().getName());
        discoveryClient.getInstances("bookshop-service").forEach(s -> {
            log.info("Host: {}, Port: {}", s.getHost(), s.getPort());
        });
    }

    private void readBooks() {
        ParameterizedTypeReference<List<Book>> ptr =
                new ParameterizedTypeReference<List<Book>>() {};
        ResponseEntity<List<Book>> list = restTemplate
                .exchange("http://bookshop-service/book/getAll", HttpMethod.GET, null, ptr);
        list.getBody().forEach(c -> log.info("Book: {}", c));
    }

    private void queryBook(Long id){
        Book book = restTemplate
                .getForObject("http://bookshop-service/book/{id}", Book.class, id);
        log.info("Book: {}", book);
    }

}
