package toughcircle.shop.model.dto.response;

import lombok.Data;
import toughcircle.shop.model.dto.ProductDto;

import java.util.List;

@Data
public class ProductListResponse {

    private String message;
    private List<ProductDto> productList;
}
