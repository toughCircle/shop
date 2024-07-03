package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Schema(description = "비밀번호 변경시 이전 비밀번호")
    @JsonProperty("old_password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String oldPassword;

    @Schema(description = "비밀번호 변경시 새로운 비밀번호")
    @JsonProperty("new_password")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String newPassword;

    @Schema(description = "주소 변경시 주소")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String address;
}
