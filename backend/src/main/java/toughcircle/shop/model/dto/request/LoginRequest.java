package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "로그인 메일")
    @NotNull(message = "로그인 메일은 필수입니다.")
    private String email;

    @Schema(description = "로그인 비밀번호")
    @NotNull(message = "로그인 비밀번호는 필수입니다.")
    private String password;
}
