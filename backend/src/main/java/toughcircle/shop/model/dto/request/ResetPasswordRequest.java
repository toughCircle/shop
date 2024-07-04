package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Schema(description = "비밀번호 재설정 요청 시 이메일")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String email;

    @Schema(description = "인증 코드")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;

    @Schema(description = "재설정 비밀번호")
    @JsonProperty("new_password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String newPassword;
}
