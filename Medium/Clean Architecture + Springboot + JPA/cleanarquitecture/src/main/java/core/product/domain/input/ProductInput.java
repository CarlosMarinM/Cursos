package core.product.domain.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInput {
    private String type;
    private String brand;
}
