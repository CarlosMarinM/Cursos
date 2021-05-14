package com.carlos.store.product;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.carlos.store.product.entity.Category;
import com.carlos.store.product.entity.Product;
import com.carlos.store.product.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryMockTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void whenFindByCategory_thenReturnProductList() {
		Product producto01 = Product.builder()
				.name("computer")
				.category(Category.builder().id(1L).build())
				.description("")
				.stock(10.0)
				.price(1240.99)
				.status("Created")
				.createAt(new Date()).build();
		productRepository.save(producto01);
		
		List<Product> founds = productRepository.findByCategory(producto01.getCategory());
		
		Assertions.assertThat(founds.size()).isEqualTo(3);
	}
}
