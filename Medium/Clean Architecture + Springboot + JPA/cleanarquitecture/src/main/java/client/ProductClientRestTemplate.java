package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.product.domain.input.ProductInput;
import core.product.domain.output.ProductOutput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class ProductClientRestTemplate implements ProductClient {

    private String riskUrl;
    private RestTemplate restTemplate;

    public ProductClientRestTemplate(String riskUrl, RestTemplate restTemplate) {
        this.riskUrl = riskUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public ProductInput getProductIn(String productId) throws JsonProcessingException {
        final String json = new ObjectMapper().writeValueAsString(new ProductOutput(productId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        return restTemplate.postForObject(riskUrl, entity, ProductInput.class);
    }
}
