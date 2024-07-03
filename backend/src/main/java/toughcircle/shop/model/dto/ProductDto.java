package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Schema(description = "상품 일련번호")
    @JsonProperty("product_id")
    private Long productId;

    @Schema(description = "상품 이름")
    private String name;
    @Schema(description = "상품 설명")
    private String description;
    @Schema(description = "가격")
    private int price;
    @Schema(description = "재고")
    private int stock;
    @Schema(description = "카테고리")
    @JsonProperty("category_id")
    private Long categoryId;
    @Schema(description = "메인 이미지 주소")
    @JsonProperty("main_image_url")
    private String mainImageUrl;
    @Schema(description = "상품 평점")
    @JsonProperty("average_score")
    private double averageScore;
    @Schema(description = "관심상품 유무")
    private Boolean liked;
}
