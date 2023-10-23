package com.carlosmarin.webflux.app;

import com.carlosmarin.webflux.app.models.documents.Category;
import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Slf4j
@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {
    @Autowired
    private ProductService productService;
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        reactiveMongoTemplate.dropCollection("products").subscribe();
        reactiveMongoTemplate.dropCollection("categories").subscribe();

        Category electronica = new Category("Electronica");
        Category computacion = new Category("Computacion");
        Category deporte = new Category("Deporte");
        Category muebles = new Category("Muebles");

        Flux.just(electronica, computacion, deporte, muebles)
                .flatMap(productService::saveCategoria)
                .doOnNext(category -> log.info("Categoria creada: " + category.getNombre() + ", Id: " + category.getId()))
                .thenMany(
                        Flux.just(
                                        new Product("TV panasonic pantalla LCD", 456.89, electronica),
                                        new Product("Sony camara HD Digital", 177.89, electronica),
                                        new Product("Apple iPod", 46.89, electronica),
                                        new Product("Sony Notebook", 846.89, computacion),
                                        new Product("Hewlett Packard Multifuncional", 200.89, computacion),
                                        new Product("Bianchi Bicicleta", 70.89, deporte),
                                        new Product("HP Notebook Omen 17", 2500.89, computacion),
                                        new Product("Mica comoda 4 cajones", 150.89, muebles),
                                        new Product("TV Sony Bravia OLED 4K Ultra HD", 2255.89, electronica)
                                )
                                .flatMap(product -> {
                                    product.setCreateAt(LocalDate.now());
                                    return productService.save(product);
                                })
                )
                .subscribe(product -> log.info("Insert: " + product.getId() + " " + product.getName()));

    }
}
