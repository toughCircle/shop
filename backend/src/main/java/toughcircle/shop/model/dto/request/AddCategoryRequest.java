package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @Schema(description = "카테고리 이름")
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

}
