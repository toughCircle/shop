package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @Schema(description = "카태고리 이름")
    private String name;
}
