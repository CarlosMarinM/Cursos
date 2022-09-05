package com.udemy.microservices.servicioproductos.models.service;


import com.udemy.microservices.commons.models.entity.Producto;

import java.util.List;

public interface ProductoService {

    public List<Producto> findAll();

    public Producto findById(Long id);

    public Producto save(Producto producto);

    public void deleteById(Long id);
}
