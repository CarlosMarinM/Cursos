package com.carlos.store.shopping.service;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.store.shopping.client.CustomerClient;
import com.carlos.store.shopping.client.ProductClient;
import com.carlos.store.shopping.entity.Invoice;
import com.carlos.store.shopping.entity.InvoiceItem;
import com.carlos.store.shopping.repository.InvoiceItemsRepository;
import com.carlos.store.shopping.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	InvoiceItemsRepository invoiceItemsRepository;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	ProductClient productClient;

	@Override
	public List<Invoice> findInvoiceAll() {
		List<Invoice> invoiceList = invoiceRepository.findAll();

		invoiceList.forEach(invoice -> {
			invoice.setCustomer(customerClient.getCustomer(invoice.getCustomerId()).getBody());
			invoice.getItems()
					.forEach(item -> item.setProduct(productClient.getProduct(item.getProductId()).getBody()));
		});

		return invoiceList;
	}

	@Override
	public Invoice createInvoice(Invoice invoice) {
		Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
		if (invoiceDB != null) {
			return invoiceDB;
		}
		invoice.setState("CREATED");
		invoiceDB = invoiceRepository.save(invoice);

		Consumer<InvoiceItem> updateStockProduct = invoiceItem -> {
			productClient.updateStockProduct(invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
		};
		invoiceDB.getItems().forEach(updateStockProduct);

		return invoiceDB;
	}

	@Override
	public Invoice updateInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if (invoiceDB == null) {
			return null;
		}
		invoiceDB.setCustomerId(invoice.getCustomerId());
		invoiceDB.setDescription(invoice.getDescription());
		invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
		invoiceDB.getItems().clear();
		invoiceDB.setItems(invoice.getItems());
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice deleteInvoice(Invoice invoice) {
		Invoice invoiceDB = getInvoice(invoice.getId());
		if (invoiceDB == null) {
			return null;
		}
		invoiceDB.setState("DELETED");
		return invoiceRepository.save(invoiceDB);
	}

	@Override
	public Invoice getInvoice(Long id) {
		Invoice invoice = invoiceRepository.findById(id).orElse(null);

		if (invoice != null) {
			invoice.setCustomer(customerClient.getCustomer(invoice.getCustomerId()).getBody());
			invoice.getItems()
					.forEach(item -> item.setProduct(productClient.getProduct(item.getProductId()).getBody()));
		}

		return invoice;
	}
}