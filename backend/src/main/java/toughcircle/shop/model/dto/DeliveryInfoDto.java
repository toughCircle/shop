package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DeliveryInfoDto {

    @Schema(description = "수신자 이름")
    @JsonProperty("recipient_name")
    @NotBlank(message = "수신자 이름은 필수입니다.")
    private String recipientName;

    @Schema(description = "수신자 연락처")
    @NotBlank(message = "수신자 연락처는 필수입니다.")
    @Pattern(
        regexp = "\\d{3}-\\d{4}-\\d{4}",
        message = "연락처는 000-0000-0000 형식이어야 합니다."
    )
    private String phone;

    @Schema(description = "배송지")
    @NotBlank(message = "배송지는 필수입니다.")
    private AddressDto address;

    @Schema(description = "배송 메모")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String orderMemo;
}
