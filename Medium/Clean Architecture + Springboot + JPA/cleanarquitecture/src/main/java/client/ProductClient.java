package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.product.domain.input.ProductInput;

public interface ProductClient {

    ProductInput getProductIn(String productId) throws JsonProcessingException;
}
