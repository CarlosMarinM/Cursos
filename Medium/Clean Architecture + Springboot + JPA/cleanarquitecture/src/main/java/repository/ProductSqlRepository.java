package repository;

import core.product.domain.Product;

public interface ProductSqlRepository {

    Product findProduct(String type, String brand);
}
