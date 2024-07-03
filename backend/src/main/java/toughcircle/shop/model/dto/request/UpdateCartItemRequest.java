package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @Schema(description = "상품 수량")
    private int quantity;
}
