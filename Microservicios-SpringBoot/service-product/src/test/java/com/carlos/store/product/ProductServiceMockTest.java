package com.carlos.store.product;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.carlos.store.product.entity.Category;
import com.carlos.store.product.entity.Product;
import com.carlos.store.product.repository.ProductRepository;
import com.carlos.store.product.service.ProductService;
import com.carlos.store.product.service.ProductServiceImpl;

@SpringBootTest
public class ProductServiceMockTest {

	@Mock
	private ProductRepository productRepository;
	private ProductService productService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		productService = new ProductServiceImpl(productRepository);
		Product computer = Product.builder()
				.id(1L)
				.name("computer")
				.category(Category.builder().id(1L).build())
				.stock(5.0)
				.price(12.5)
				.build();
		
		Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(computer));
		Mockito.when(productRepository.save(computer)).thenReturn(computer);
	}
	
	@Test
	public void whenValidGetId_thenReturnProduct() {
		Product found = productService.getProduct(1L);
		Assertions.assertThat(found.getName()).isEqualTo("computer");
	}
	
	@Test
	public void whenValidUpdateStock_thenReturnNewStock() {
		Product newStcok = productService.updateStock(1L, 8.0);
		Assertions.assertThat(newStcok.getStock()).isEqualTo(13.0);
	}
}
