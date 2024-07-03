package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Schema(description = "사용자 일련번호")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "사용자 이름")
    private String username;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "기본 주소")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String address;

    @Schema(description = "연락처")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String phone;
}
