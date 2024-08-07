package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewReviewRequest {

    @Schema(description = "상품 별점")
    @Min(value = 0, message = "별점은 0 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 5 이하이어야 합니다.")
    private double rating;

    @Schema(description = "상품 후기 내용")
    @NotBlank(message = "후기 내용은 필수입니다.")
    private String comment;

    @Schema(description = "상품 후기 이미지")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String image;
}
