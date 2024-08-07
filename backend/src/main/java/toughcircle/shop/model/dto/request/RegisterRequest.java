package toughcircle.shop.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @Schema(description = "사용자 이름")
    @NotBlank(message = "사용자 이름은 필수입니다.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*()\\-_=+\\[\\]{};:'\",.<>?/\\\\|`~])[A-Za-z\\d!@#\\$%\\^&\\*()\\-_=+\\[\\]{};:'\",.<>?/\\\\|`~]{5,}$",
        message = "비밀번호는 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 2개 이상을 포함하고, 최소 5자리 이상이어야 합니다."
    )
    private String password;

    @Schema(description = "이메일")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
}
