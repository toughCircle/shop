package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartItemDto {

    @Schema(description = "장바구니 상품 일련번호")
    @JsonProperty("cart_item_id")
    private Long cartItemId;

    @Schema(description = "수량")
    private int quantity;

    @Schema(description = "상품")
    private ProductDto product;
}
