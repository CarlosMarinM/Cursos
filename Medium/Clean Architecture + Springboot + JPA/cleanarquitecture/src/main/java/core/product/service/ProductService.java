package core.product.service;

import client.ProductClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import core.product.domain.Product;
import core.product.domain.input.ProductInput;
import repository.ProductSqlRepository;

public class ProductService {

    private final ProductClient productClient;
    private final ProductSqlRepository productSqlRepository;

    public ProductService(ProductClient productClient, ProductSqlRepository productSqlRepository) {
        this.productClient = productClient;
        this.productSqlRepository = productSqlRepository;
    }

    public Product getProduct(String idProduct) throws JsonProcessingException {
        final ProductInput productIn = productClient.getProductIn(idProduct);
        return productSqlRepository.findProduct(productIn.getType(), productIn.getBrand());
    }
}
