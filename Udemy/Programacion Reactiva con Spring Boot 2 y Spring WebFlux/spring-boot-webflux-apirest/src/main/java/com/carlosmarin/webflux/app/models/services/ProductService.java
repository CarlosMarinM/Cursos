package com.carlosmarin.webflux.app.models.services;

import com.carlosmarin.webflux.app.models.documents.Category;
import com.carlosmarin.webflux.app.models.documents.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
	
	public Flux<Product> findAll();
	
	public Flux<Product> findAllConNombreUpperCase();
	
	public Flux<Product> findAllConNombreUpperCaseRepeat();
	
	public Mono<Product> findById(String id);
	
	public Mono<Product> save(Product producto);
	
	public Mono<Void> delete(Product producto);
	
	public Flux<Category> findAllCategoria();
	
	public Mono<Category> findCategoriaById(String id);
	
	public Mono<Category> saveCategoria(Category categoria);

}
