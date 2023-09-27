package com.carlosmarin.webflux.app.models.dao;

import com.carlosmarin.webflux.app.models.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductDao extends ReactiveMongoRepository<Product, String> {
}
