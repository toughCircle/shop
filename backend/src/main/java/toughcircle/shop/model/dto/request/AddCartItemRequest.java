package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddCartItemRequest {

    @Schema(description = "상품 일련번호")
    @JsonProperty("product_id")
    private Long productId;

    @Schema(description = "수량", example = "2")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;

}
