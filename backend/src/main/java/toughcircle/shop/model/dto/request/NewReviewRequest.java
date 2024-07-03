package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewReviewRequest {

    @Schema(description = "상품 점수")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private double rating;

    @Schema(description = "상품 후기 내용")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String comment;

    @Schema(description = "상품 후기 이미지")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String image;
}
