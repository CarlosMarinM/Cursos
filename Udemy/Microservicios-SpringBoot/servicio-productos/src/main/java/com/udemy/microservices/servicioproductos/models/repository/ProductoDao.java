package com.udemy.microservices.servicioproductos.models.repository;

import com.udemy.microservices.commons.models.entity.Producto;
import org.springframework.data.repository.CrudRepository;

public interface ProductoDao extends CrudRepository<Producto, Long> {
}
