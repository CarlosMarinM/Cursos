package com.carlosmarin.webflux.app.controllers;

import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Value("${uploads.path}")
    private String uploadsPath;
    @Autowired
    private ProductService productService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Product>>> findAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.findAll())
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable final String id) {
        return productService.findById(id)
                .map(product ->
                        ResponseEntity
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(product)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> create(@Valid @RequestBody final Mono<Product> monoProduct) {
        Map<String, Object> response = new HashMap<>();

        return monoProduct.flatMap(product -> {
            product.setCreateAt(Optional.ofNullable(product.getCreateAt()).orElse(LocalDate.now()));
            return productService.save(product)
                    .map(product1 -> {
                                response.put("product", product1);
                                response.put("message", "Producto creado con exito");
                                response.put("timestamp", LocalDate.now());
                                return ResponseEntity
                                        .created(URI.create("/api/products/".concat(product1.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(response);
                            }
                    )
                    .onErrorResume(throwable -> {
                                return Mono.just(throwable).cast(WebExchangeBindException.class)
                                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                                        .flatMapMany(Flux::fromIterable)
                                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                                        .collectList()
                                        .flatMap(strings -> {
                                            response.put("errors", strings);
                                            response.put("timestamp", LocalDate.now());
                                            response.put("status", HttpStatus.OK.value());
                                            return Mono.just(ResponseEntity.ok().body(response));
                                        });
                            }
                    );
        });
    }

    @PostMapping("/v2")
    public Mono<ResponseEntity<Product>> createWithPhoto(final Product product, @RequestPart FilePart file) {
        product.setCreateAt(Optional.ofNullable(product.getCreateAt()).orElse(LocalDate.now()));
        product.setPhoto(UUID.randomUUID().toString() + "-" + file.filename()
                .replace(" ", "")
                .replace(":", "")
                .replace("\\", ""));
        return file.transferTo(new File(uploadsPath + product.getPhoto()))
                .then(productService.save(product))
                .map(product1 ->
                        ResponseEntity
                                .created(URI.create("/api/products/".concat(product1.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(product1)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> edit(@RequestBody final Product product, @PathVariable final String id) {
        return productService.findById(id)
                .flatMap(product1 -> {
                    product1.setName(product.getName());
                    product1.setPrice(product.getPrice());
                    product1.setCategory(product.getCategory());
                    return productService.save(product1);
                })
                .map(product1 ->
                        ResponseEntity
                                .created(URI.create("/api/products/".concat(product1.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(product1)
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable final String id) {
        return productService.findById(id)
                .flatMap(product ->
                        productService.delete(product).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/upload/image/{id}")
    public Mono<ResponseEntity<Product>> upload(@PathVariable final String id, @RequestPart final FilePart file) {
        return productService.findById(id)
                .flatMap(product -> {
                    product.setPhoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "")
                            .replace(":", "")
                            .replace("\\", ""));
                    return file.transferTo(new File(uploadsPath + product.getPhoto()))
                            .then(productService.save(product));
                })
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
