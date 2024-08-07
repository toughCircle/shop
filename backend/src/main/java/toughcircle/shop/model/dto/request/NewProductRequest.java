package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewProductRequest {

    @Schema(description = "상품 이름")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;

    @Schema(description = "상품 설명")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @Schema(description = "가격", example = "1000000")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;

    @Schema(description = "재고", example = "50")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private int stock;

    @Schema(description = "카테고리")
    @JsonProperty("category_id")
    @NotNull(message = "카테고리 일련번호는 필수입니다.")
    private Long categoryId;

    @Schema(description = "메인 이미지 주소")
    @JsonProperty("main_image_url")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mainImageUrl;

}
