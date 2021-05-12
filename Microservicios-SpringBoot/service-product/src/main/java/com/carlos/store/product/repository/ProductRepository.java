package com.carlos.store.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.store.product.entity.Category;
import com.carlos.store.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findByCategory(Category category);
}
