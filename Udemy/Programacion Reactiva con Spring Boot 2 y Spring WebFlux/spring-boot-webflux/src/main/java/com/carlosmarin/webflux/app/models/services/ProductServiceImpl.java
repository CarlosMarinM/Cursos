package com.carlosmarin.webflux.app.models.services;

import com.carlosmarin.webflux.app.models.dao.CategoriaDao;
import com.carlosmarin.webflux.app.models.dao.ProductDao;
import com.carlosmarin.webflux.app.models.documents.Category;
import com.carlosmarin.webflux.app.models.documents.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao dao;
	
	@Autowired
	private CategoriaDao categoriaDao;
	
	@Override
	public Flux<Product> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Product> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Product> save(Product producto) {
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(Product producto) {
		return dao.delete(producto);
	}

	@Override
	public Flux<Product> findAllConNombreUpperCase() {
		return dao.findAll().map(producto -> {
			producto.setName(producto.getName().toUpperCase());
			return producto;
		});
	}

	@Override
	public Flux<Product> findAllConNombreUpperCaseRepeat() {
		return findAllConNombreUpperCase().repeat(5000);
	}

	@Override
	public Flux<Category> findAllCategoria() {
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Category> findCategoriaById(String id) {
		return categoriaDao.findById(id);
	}

	@Override
	public Mono<Category> saveCategoria(Category categoria) {
		return categoriaDao.save(categoria);
	}


}
