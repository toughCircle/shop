package toughcircle.shop.model.dto.response;

import lombok.Data;
import toughcircle.shop.model.dto.ProductDto;

@Data
public class ProductResponse {

    private String message;
    private ProductDto product;
}
