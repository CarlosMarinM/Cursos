package com.carlosmarin.webflux.app.handlers;

import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ProductHandler {

    @Autowired
    private ProductService productService;
    @Autowired
    private Validator validator;
    @Value("${config.uploads.path}")
    private String path;

    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(), Product.class);
    }

    public Mono<ServerResponse> see(ServerRequest request) {
        String id = request.pathVariable("id");
        return productService.findById(id)
                .flatMap(product -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);

        return productMono
                .flatMap(product -> {
                    product.setCreateAt(Optional.ofNullable(product.getCreateAt()).orElse(LocalDate.now()));
                    return productService.save(product)
                            .flatMap(productDb -> ServerResponse
                                    .created(URI.create("/api/v2/products/".concat(productDb.getId())))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(fromValue(productDb))
                            );
                });
    }

    public Mono<ServerResponse> edit(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);
        String id = request.pathVariable("id");

        return productService.findById(id)
                .zipWith(productMono, (db, req) -> {
                    db.setName(req.getName());
                    db.setPrice(req.getPrice());
                    db.setCategory(req.getCategory());
                    return db;
                }).flatMap(product -> ServerResponse
                        .created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productService.save(product), Product.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productDb = productService.findById(id);

        return productDb
                .flatMap(product -> productService.delete(product).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> upload(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.multipartData()
                .map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> productService.findById(id)
                        .flatMap(product -> {
                            product.setPhoto(UUID.randomUUID().toString() + "-" + filePart.filename()
                                    .replace(" ", "-")
                                    .replace(":", "")
                                    .replace("\\", "")
                            );
                            return filePart.transferTo(new File(path + product.getPhoto()))
                                    .then(productService.save(product));

                        }))
                .flatMap(product -> ServerResponse
                        .created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
