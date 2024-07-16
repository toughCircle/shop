package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    @Schema(description = "상품후기 일련번호")
    @JsonProperty("review_id")
    private Long reviewId;
    @Schema(description = "상품후기 등록 사용자 일련번호")
    @JsonProperty("user_id")
    private Long userId;
    @Schema(description = "상품후기 등록 사용자 이름")
    private String username;
    @Schema(description = "상품 일련번호")
    @JsonProperty("product_id")
    private Long productId;
    @Schema(description = "상품 별점")
    private double rating;
    @Schema(description = "상품 후기 내용")
    private String comment;
    @Schema(description = "작성일")
    @JsonProperty("create_at")
    private LocalDateTime createAt;
    @Schema(description = "수정일")
    @JsonProperty("update_at")
    private LocalDateTime updateAt;
    @Schema(description = "상품 후기 이미지")
    private String image;
}
