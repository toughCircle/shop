package toughcircle.shop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddProductResponse {

    @Schema(description = "응답 메시지")
    private String message;
    @Schema(description = "상품 일련번호")
    @JsonProperty("product_id")
    private Long productId;
}
