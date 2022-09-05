package com.udemy.microservices.item.controllers;

import com.udemy.microservices.commons.models.entity.Producto;
import com.udemy.microservices.item.models.Item;
import com.udemy.microservices.item.models.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RefreshScope
public class ItemController {

    @Value("${configuracion.texto}")
    private String texto;

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("itemServiceFeignImpl")
    private ItemService itemService;

    @GetMapping("/listar")
    public List<Item> listar() {
        return itemService.findAll();
    }

    @GetMapping("/ver/{id}/cantidad/{cantidad}")
    public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
        return cbFactory.create("items").run(() -> itemService.findById(id, cantidad), e -> metodoAlternativoDetalle(id, cantidad, e));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativoDetalle")
    @GetMapping("/ver2/{id}/cantidad/{cantidad}")
    public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad) {
        return itemService.findById(id, cantidad);
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativoDetalle3")
    @TimeLimiter(name = "items")
    @GetMapping("/ver3/{id}/cantidad/{cantidad}")
    public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad) {
        return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
    }

    public Item metodoAlternativoDetalle(Long id, Integer catindad, Throwable e) {
        log.info(e.getMessage());
        Producto producto = new Producto(id, "Camara Sony", 0.0, null, null);
        Item item = new Item(producto, catindad);
        return item;
    }

    public CompletableFuture<Item> metodoAlternativoDetalle3(Long id, Integer cantidad, Throwable e) {
        log.info(e.getMessage());
        Producto producto = new Producto(id, "Camara Sony", 500.0, null, null);
        Item item = new Item(producto, cantidad);
        return CompletableFuture.supplyAsync(() -> item);
    }

    @GetMapping("/config")
    public ResponseEntity<?> obtenerConfiguracion(@Value("${server.port}") String puerto) {
        Map<String, String> props = new HashMap<>();
        props.put("texto", texto);
        props.put("puerto", puerto);

        Arrays.stream(env.getActiveProfiles())
                .filter(profile -> profile.equals("dev"))
                .findAny()
                .ifPresent(profile -> {
                    props.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
                    props.put("autor.email", env.getProperty("configuracion.autor.email"));
                });

        return new ResponseEntity<Map<String, String>>(props, HttpStatus.OK);
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return itemService.save(producto);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
        return itemService.update(producto, id);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        itemService.delete(id);
    }
}
