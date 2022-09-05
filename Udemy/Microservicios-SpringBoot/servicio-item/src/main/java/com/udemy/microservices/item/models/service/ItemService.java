package com.udemy.microservices.item.models.service;

import com.udemy.microservices.commons.models.entity.Producto;
import com.udemy.microservices.item.models.Item;

import java.util.List;

public interface ItemService {

    public List<Item> findAll();

    public Item findById(Long id, Integer cantidad);

    public Producto save(Producto producto);

    public Producto update(Producto producto, Long id);

    public void delete(Long id);
}
