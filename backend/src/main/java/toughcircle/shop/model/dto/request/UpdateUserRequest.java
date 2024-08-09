package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import toughcircle.shop.model.dto.AddressDto;

@Data
public class UpdateUserRequest {

    @Schema(description = "비밀번호 변경시 이전 비밀번호")
    @JsonProperty("old_password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String oldPassword;

    @Schema(description = "비밀번호 변경시 새로운 비밀번호")
    @JsonProperty("new_password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~])[A-Za-z\\d!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|`~]{5,}$",
        message = "비밀번호는 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 2개 이상을 포함하고, 최소 5자리 이상이어야 합니다."
    )
    private String newPassword;

    @Schema(description = "주소 변경시 주소")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private AddressDto address;
}
