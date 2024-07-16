package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderItemDto {

    @Schema(description = "주문 상품 일련번호")
    @JsonProperty("order_item_id")
    private Long orderItemId;
    @Schema(description = "상품 일련번호")
    private ProductDto product;
    @Schema(description = "수량")
    private int quantity;
    @Schema(description = "상품 총 금액")
    @JsonProperty("total_price")
    private int totalPrice;

}
