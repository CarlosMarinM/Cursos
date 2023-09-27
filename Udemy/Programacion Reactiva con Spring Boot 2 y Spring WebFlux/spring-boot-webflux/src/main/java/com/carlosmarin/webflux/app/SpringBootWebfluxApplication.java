package com.carlosmarin.webflux.app;

import com.carlosmarin.webflux.app.models.dao.ProductDao;
import com.carlosmarin.webflux.app.models.documents.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {
    @Autowired
    private ProductDao dao;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        reactiveMongoTemplate.dropCollection("products").subscribe();

        Flux.just(
                        new Product("TV panasonic pantalla LCD", 456.89),
                        new Product("Sony camara HD Digital", 177.89),
                        new Product("Apple iPod", 46.89),
                        new Product("Sony Notebook", 846.89),
                        new Product("Hewlett Packard Multifuncional", 200.89),
                        new Product("Bianchi Bicicleta", 70.89),
                        new Product("HP Notebook Omen 17", 2500.89),
                        new Product("Mica comoda 4 cajones", 150.89),
                        new Product("TV Sony Bravia OLED 4K Ultra HD", 2255.89)
                )
                .flatMap(product -> {
                    product.setCreateAt(LocalDate.now());
                    return dao.save(product);
                })
                .subscribe(product -> log.info("Insert: " + product.getId() + " " + product.getName()));
    }
}
