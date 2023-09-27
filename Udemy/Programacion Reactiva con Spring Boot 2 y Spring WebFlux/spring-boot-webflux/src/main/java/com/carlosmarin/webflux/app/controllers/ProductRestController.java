package com.carlosmarin.webflux.app.controllers;

import com.carlosmarin.webflux.app.models.dao.ProductDao;
import com.carlosmarin.webflux.app.models.documents.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductDao productDao;

    @GetMapping
    public Flux<Product> index() {
        return productDao.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                })
                .doOnNext(product -> log.info(product.getName()));
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable String id) {
//        return productDao.findById(id);
        Flux<Product> productsFlux = productDao.findAll();
        return productsFlux
                .filter(product -> product.getId().equals(id))
                .next()
                .doOnNext(product -> log.info(product.getName()));
    }
}
