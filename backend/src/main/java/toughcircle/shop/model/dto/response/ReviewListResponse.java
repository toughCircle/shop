package toughcircle.shop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toughcircle.shop.model.dto.ReviewDto;

import java.util.List;

@Data
public class ReviewListResponse {

    @Schema(description = "응답 메시지")
    private String message;
    @Schema(description = "상품 평점")
    @JsonProperty("average_score")
    private double averageScore;
    @Schema(description = "상품 후기 리스트")
    private List<ReviewDto> reviewList;
}
