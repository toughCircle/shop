package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GuestDto {

    @Schema(description = "비회원 일련번호")
    @JsonProperty("guest_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long guestId;

    @Schema(description = "이름")
    private String name;
    @Schema(description = "연락처")
    private String phone;
    @Schema(description = "메일")
    private String email;
}
