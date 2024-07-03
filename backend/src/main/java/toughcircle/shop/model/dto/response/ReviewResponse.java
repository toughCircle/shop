package toughcircle.shop.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toughcircle.shop.model.dto.ReviewDto;

@Data
public class ReviewResponse {

    @Schema(description = "응답 메시지")
    private String message;
    @Schema(description = "상품 후기")
    private ReviewDto review;
}
