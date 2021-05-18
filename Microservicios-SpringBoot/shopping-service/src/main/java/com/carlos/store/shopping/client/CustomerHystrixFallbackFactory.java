package com.carlos.store.shopping.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.carlos.store.shopping.model.Customer;

@Component
public class CustomerHystrixFallbackFactory implements CustomerClient {

	@Override
	public ResponseEntity<Customer> getCustomer(long id) {
		Customer customer = Customer.builder()
				.firstName("none")
				.lastName("none")
				.email("none")
				.build();

		return ResponseEntity.ok(customer);
	}

}
