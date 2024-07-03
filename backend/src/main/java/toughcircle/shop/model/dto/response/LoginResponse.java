package toughcircle.shop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "사용자 일련번호")
    @JsonProperty("user_id")
    private Long userId;
}
