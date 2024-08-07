package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Schema(description = "비밀번호 재설정 요청 시 이메일")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @Schema(description = "인증 코드")
    @NotBlank(message = "인증 코드는 필수입니다.")
    private String code;

    @Schema(description = "재설정 비밀번호")
    @JsonProperty("new_password")
    @NotBlank(message = "재설정 비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~])[A-Za-z\\d!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~]{5,}$",
        message = "비밀번호는 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 2개 이상을 포함하고, 최소 5자리 이상이어야 합니다."
    )
    private String newPassword;
}
