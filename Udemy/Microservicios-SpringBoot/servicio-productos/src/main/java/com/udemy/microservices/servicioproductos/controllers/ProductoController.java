package com.udemy.microservices.servicioproductos.controllers;

import com.udemy.microservices.commons.models.entity.Producto;
import com.udemy.microservices.servicioproductos.models.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class ProductoController {

    @Autowired
    private Environment env;
    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public List<Producto> listar() {
        return productoService.findAll().stream().map(producto -> {
            producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
            return producto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/ver/{id}")
    public Producto detalle(@PathVariable Long id) throws InterruptedException {
        if (id.equals(10L)) {
            throw new IllegalStateException("Producto no encontrado!");
        }
        if (id.equals(7L)) {
            TimeUnit.SECONDS.sleep(5L);
        }
        Producto producto = productoService.findById(id);
        producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
        return producto;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
        Producto productoDb = productoService.findById(id);

        productoDb.setNombre(producto.getNombre());
        productoDb.setPrecio(producto.getPrecio());

        return productoService.save(productoDb);
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoService.deleteById(id);
    }
}
