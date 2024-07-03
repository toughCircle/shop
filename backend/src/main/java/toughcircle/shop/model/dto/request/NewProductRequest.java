package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewProductRequest {

    @Schema(description = "상품 이름")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    @Schema(description = "상품 설명")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;
    @Schema(description = "가격")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int price;
    @Schema(description = "재고")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int stock;
    @Schema(description = "카테고리")
    @JsonProperty("category_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long categoryId;
    @Schema(description = "메인 이미지 주소")
    @JsonProperty("main_image_url")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mainImageUrl;

}
