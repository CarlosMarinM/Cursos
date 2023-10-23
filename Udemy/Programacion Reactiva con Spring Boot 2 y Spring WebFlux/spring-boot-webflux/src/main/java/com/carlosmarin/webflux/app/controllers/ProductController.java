package com.carlosmarin.webflux.app.controllers;

import com.carlosmarin.webflux.app.models.documents.Category;
import com.carlosmarin.webflux.app.models.documents.Product;
import com.carlosmarin.webflux.app.models.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@SessionAttributes("product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${uploads.path}")
    private String uploadsPath;

    @ModelAttribute("categories")
    public Flux<Category> categories() {
        return productService.findAllCategoria();
    }

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
        model.addAttribute("buttonName", "Create");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(@Valid Product product,
                             BindingResult result,
                             Model model,
                             @RequestPart FilePart file,
                             SessionStatus sessionStatus) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Errores en el formulario producto");
            model.addAttribute("buttonName", "Guardar");
            return Mono.just("form");
        } else {
            sessionStatus.setComplete();

            return productService.findCategoriaById(product.getCategory().getId())
                    .flatMap(category -> {
                        product.setCreateAt(Optional.ofNullable(product.getCreateAt()).orElse(LocalDate.now()));
                        product.setPhoto(Optional.ofNullable(file)
                                .filter(f -> !f.filename().isEmpty())
                                .map(f -> UUID.randomUUID().toString() + "-" + f.filename()
                                        .replace(" ", "")
                                        .replace(":", "")
                                        .replace("\\", "")
                                )
                                .orElse(null));
                        product.setCategory(category);
                        return productService.save(product);
                    })
                    .doOnNext(product1 -> {
                        log.info("Categoria asignada: " + product1.getCategory().getNombre() + " Id: " + product1.getCategory().getId());
                        log.info("Producto guardado: " + product1.getName() + " Id: " + product1.getId());
                    })
                    .flatMap(product1 -> Optional.ofNullable(file)
                            .filter(f -> !f.filename().isEmpty())
                            .map(f -> f.transferTo(new File(uploadsPath + product1.getPhoto())))
                            .orElse(Mono.empty())
                    )
                    .thenReturn("redirect:/list?success=Producto+guardado+con+exito");
        }
    }

    @GetMapping({"/form/{id}"})
    public Mono<String> edit(@PathVariable String id, Model model) {
        Mono<Product> productMono = productService.findById(id)
                .doOnNext(product -> log.info("Producto: " + product.getName()))
                .defaultIfEmpty(new Product());
        model.addAttribute("product", productMono);
        model.addAttribute("title", "Edit Product");
        model.addAttribute("buttonName", "Edit");
        return Mono.just("form");
    }

    @GetMapping({"/form-v2/{id}"})
    public Mono<String> editV2(@PathVariable String id, Model model) {
        return productService.findById(id)
                .doOnNext(product -> {
                    log.info("Producto: " + product.getName());
                    model.addAttribute("product", product);
                    model.addAttribute("title", "Edit Product");
                    model.addAttribute("buttonName", "Edit");
                })
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (product.getId() == null) {
                        return Mono.error(new InterruptedException("No existe el producto"));
                    }
                    return Mono.just(product);
                })
                .then(Mono.just("form"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=no+existe+el+producto"));
    }

    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productService.findById(id)
                .defaultIfEmpty(new Product())
                .flatMap(product -> {
                    if (product.getId() == null) {
                        return Mono.error(new InterruptedException("No existe el producto"));
                    }
                    return Mono.just(product);
                })
                .flatMap(productService::delete)
                .then(Mono.just("redirect:/list?success=Producto=eliminado+con+exito"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=no+existe+el+producto+a+eliminar"));
    }

    @GetMapping("/see/{id}")
    public Mono<String> see(Model model, @PathVariable String id) {
        return productService.findById(id)
                .doOnNext(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("title", "Detalle producto");
                })
                .switchIfEmpty(Mono.just(new Product()))
                .flatMap(product -> Optional.ofNullable(product.getId()).map(s -> Mono.just(product)).orElse(Mono.error(new InterruptedException("No existe el producto"))))
                .then(Mono.just("see"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=no+existe+el+producto"));
    }

    @GetMapping("/see/image/{photoName:.+}")
    public Mono<ResponseEntity<Resource>> seePhoto(@PathVariable String photoName) throws MalformedURLException {
        Path path = Paths.get(uploadsPath).resolve(photoName).toAbsolutePath();
        Resource image = new UrlResource(path.toUri());
        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                        .body(image)
        );
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
