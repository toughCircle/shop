package toughcircle.shop.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "에러 메시지")
    private String message;

    @Schema(description = "에러 코드")
    private String status;
}
