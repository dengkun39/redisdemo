package com.ken.bookcustomer;

import com.ken.bookcustomer.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Component
@Slf4j
public class BookRestRunner implements ApplicationRunner {
    private static final URI ROOT_URI = URI.create("http://bookshop-service/");
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        showServiceInstances();

        //Optional<Link> bookLink = getLink(ROOT_URI, "rest-books");
        //throw No servers available for service: 10.86.27.215.  don't know why

        //readBookMenu(bookLink.get());
    }

    private void showServiceInstances() {
        log.info("DiscoveryClient: {}", discoveryClient.getClass().getName());
        discoveryClient.getInstances("bookshop-service").forEach(s -> {
            log.info("Host: {}, Port: {}", s.getHost(), s.getPort());
        });
    }


    private void readBookMenu(Link coffeeLink) {
        ResponseEntity<PagedModel<EntityModel<Book>>> coffeeResp =
                restTemplate.exchange(coffeeLink.getTemplate().expand(),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedModel<EntityModel<Book>>>() {});
        log.info("Menu Response: {}", coffeeResp.getBody());
    }


    private  Optional<Link> getLink(URI uri, String rel) {
        ResponseEntity<CollectionModel<Link>> rootResp =
                restTemplate.exchange(uri, HttpMethod.GET, null,
                        new ParameterizedTypeReference<CollectionModel<Link>>() {
                        });
        Optional<Link> link = rootResp.getBody().getLink(rel);
        log.info("Link: {}", link);
        return link;
    }
}