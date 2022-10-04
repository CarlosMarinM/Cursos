package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.contract.domain.input.ContractInput;
import core.contract.domain.output.ContractOutput;
import core.product.domain.Product;
import core.product.service.ProductService;

public class ContractController {

    private ProductService productService;

    public ContractController(ProductService productService) {
        this.productService = productService;
    }

    private ContractOutput generateContract(ContractInput request) throws JsonProcessingException {
        Product product = productService.getProduct(request.getProductId());
    }
}
