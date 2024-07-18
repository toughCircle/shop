package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryDto {

    @Schema(description = "카테고리 일련번호")
    @JsonProperty("category_id")
    private Long categoryId;

    @Schema(description = "카테고리 이름")
    private String name;
}
