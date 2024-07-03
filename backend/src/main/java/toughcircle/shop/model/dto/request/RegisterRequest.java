package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterRequest {

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "비밃번호")
    private String password;

    @Schema(description = "이메일")
    private String email;
}
