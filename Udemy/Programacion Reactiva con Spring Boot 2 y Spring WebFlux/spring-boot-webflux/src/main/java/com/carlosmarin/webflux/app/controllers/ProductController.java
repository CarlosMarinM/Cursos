package com.carlosmarin.webflux.app.controllers;

import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Controller
@SessionAttributes("product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping({"/list", "/"})
    public Mono<String> list(Model model) {
        Flux<Product> products = productService.findAllConNombreUpperCase();

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products", products);
        model.addAttribute("title", "Products list");
        return Mono.just("list");
    }

    @GetMapping({"/form"})
    public Mono<String> create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Product form");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(Product product, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return productService.save(product)
                .doOnNext(product1 -> {
                    log.info("Producto guardado: " + product1.getName() + " Id: " + product1.getId());
                }).thenReturn("redirect:/list");
    }

    @GetMapping({"/form/{id}"})
    public Mono<String> edit(@PathVariable String id, Model model){
        Mono<Product> productMono = productService.findById(id)
                .doOnNext(product -> log.info("Producto: " + product.getName()))
                .defaultIfEmpty(new Product());
        model.addAttribute("product", productMono);
        model.addAttribute("title", "Edit Product");
        return Mono.just("form");
    }

    // Manejo de la contrapresion con ReactiveDataDriver
    @GetMapping("/data-driver-list")
    public String dataDriverList(Model model) {
        Flux<Product> products = productService.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 1));
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/full-list")
    public String fullList(Model model) {
        Flux<Product> products = productService.findAllConNombreUpperCaseRepeat();

        model.addAttribute("products", products);
        model.addAttribute("title", "Products list");
        return "list";
    }

    @GetMapping("/chunked-list")
    public String chunkedList(Model model) {
        Flux<Product> products = productService.findAllConNombreUpperCaseRepeat();

        model.addAttribute("products", products);
        model.addAttribute("title", "Products list");
        return "chunked-list";
    }
}
