package com.carlosmarin.webflux.app.handlers;

import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
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
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ProductHandler {
    @Autowired
    private ProductService service;
    @Value("${config.uploads.path}")
    private String path;
    @Autowired
    private Validator validator;

    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Product.class);
    }

    public Mono<ServerResponse> see(ServerRequest request) {
        String id = request.pathVariable("id");
        return service.findById(id)
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
                    Errors errors = new BeanPropertyBindingResult(product, Product.class.getName());
                    validator.validate(product, errors);

                    if (errors.hasErrors()) {
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage())
                                .collectList()
                                .flatMap(list -> ServerResponse.badRequest().body(fromValue(list)));
                    } else {
                        if (product.getCreateAt() == null) {
                            product.setCreateAt(LocalDate.now());
                        }
                        return service.save(product)
                                .flatMap(productDb -> ServerResponse
                                        .created(URI.create("/api/v2/products/".concat(productDb.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(fromValue(productDb))
                                );
                    }
                });
    }

    public Mono<ServerResponse> edit(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class);
        String id = request.pathVariable("id");

        Mono<Product> productDb = service.findById(id);

        return productDb
                .zipWith(productMono, (db, req) -> {
                    db.setName(req.getName());
                    db.setPrice(req.getPrice());
                    return db;
                }).flatMap(product -> ServerResponse
                        .created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.save(product), Product.class)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productDb = service.findById(id);

        return productDb
                .flatMap(product -> service.delete(product).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> upload(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.multipartData()
                .map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> service.findById(id)
                        .flatMap(product -> {
                            product.setPhoto(UUID.randomUUID().toString() + "-" + filePart.filename()
                                    .replace(" ", "-")
                                    .replace(":", "")
                                    .replace("\\", "")
                            );
                            return filePart.transferTo(new File(path + product.getPhoto()))
                                    .then(service.save(product));

                        }))
                .flatMap(product -> ServerResponse
                        .created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createWithPhoto(ServerRequest request) {
        Mono<Product> productMono = request.multipartData().map(multipart -> {
            FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
            FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
            return new Product(name.value(), Double.parseDouble(price.value()));
        });

        return request.multipartData()
                .map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> productMono
                        .flatMap(product -> {
                            product.setPhoto(UUID.randomUUID().toString() + "-" + filePart.filename()
                                    .replace(" ", "-")
                                    .replace(":", "")
                                    .replace("\\", "")
                            );
                            product.setCreateAt(LocalDate.now());
                            return filePart.transferTo(new File(path + product.getPhoto()))
                                    .then(service.save(product));

                        }))
                .flatMap(product -> ServerResponse
                        .created(URI.create("/api/v2/products/".concat(product.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)));
    }
}
