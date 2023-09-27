package com.carlosmarin.webflux.app.models.dao;

import com.carlosmarin.webflux.app.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Category, String>{

}
