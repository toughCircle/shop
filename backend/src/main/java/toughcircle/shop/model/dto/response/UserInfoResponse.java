package toughcircle.shop.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import toughcircle.shop.model.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "사용자 정보")
    private UserDto user;
}
