package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @Schema(description = "상품 수량")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;

}
