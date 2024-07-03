package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddCartItemRequest {

    @Schema(description = "상품 일련번호")
    @JsonProperty("product_id")
    private Long productId;
    @Schema(description = "상품 수량")
    private int quantity;
}
