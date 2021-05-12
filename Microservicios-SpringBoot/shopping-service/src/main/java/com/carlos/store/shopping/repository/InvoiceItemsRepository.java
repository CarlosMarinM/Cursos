package com.carlos.store.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.store.shopping.entity.InvoiceItem;

public interface InvoiceItemsRepository extends JpaRepository<InvoiceItem, Long> {
}
