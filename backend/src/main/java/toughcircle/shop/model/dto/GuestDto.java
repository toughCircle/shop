package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GuestDto {

    @Schema(description = "비회원 일련번호")
    @JsonProperty("guest_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long guestId;

    @Schema(description = "비회원 이름")
    @NotBlank(message = "비회원 이름은 필수입니다.")
    private String name;

    @Schema(description = "비회원 연락처")
    @NotBlank(message = "비회원 연락처는 필수입니다.")
    @Pattern(
        regexp = "\\d{3}-\\d{4}-\\d{4}",
        message = "연락처는 000-0000-0000 형식이어야 합니다."
    )
    private String phone;

    @Schema(description = "비회원 이메일")
    @NotBlank(message = "비회원 이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;
}
