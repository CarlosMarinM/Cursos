package com.carlosmarin.webflux.app.models.dao;

import com.carlosmarin.webflux.app.models.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryDao extends ReactiveMongoRepository<Category, String>{

}
