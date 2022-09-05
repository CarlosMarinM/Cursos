package com.udemy.microservices.item.models.service;

import com.udemy.microservices.commons.models.entity.Producto;
import com.udemy.microservices.item.clientes.ProductoClienteRest;
import com.udemy.microservices.item.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceFeignImpl implements ItemService{

    @Autowired
    private ProductoClienteRest clienteFeign;

    @Override
    public List<Item> findAll() {
        return clienteFeign.listar().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(clienteFeign.detalle(id), cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return clienteFeign.crear(producto);
    }

    @Override
    public Producto update(Producto producto, Long id) {
        return clienteFeign.update(producto, id);
    }

    @Override
    public void delete(Long id) {
        clienteFeign.eliminar(id);
    }
}
