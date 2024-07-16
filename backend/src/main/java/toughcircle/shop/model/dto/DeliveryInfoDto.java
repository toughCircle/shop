package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeliveryInfoDto {
//    @Schema(description = "배송 정보 일련번호")
//    @JsonProperty("delivery_info_id")
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    private Long DeliveryInfoId;

    @Schema(description = "수신자 이름")
    @JsonProperty("recipient_name")
    private String recipientName;
    @Schema(description = "수신자 연락처")
    private String phone;
    @Schema(description = "배송지")
    private String address;
    @Schema(description = "배송 메모")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String orderMemo;
}
