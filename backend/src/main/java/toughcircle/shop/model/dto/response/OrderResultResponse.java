package toughcircle.shop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toughcircle.shop.model.dto.DeliveryInfoDto;

@Data
public class OrderResultResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "회원 일련번호")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "비회원 일련번호")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("guest_id")
    private Long guestId;

    @Schema(description = "주문 총 금액")
    @JsonProperty("total_price")
    private int totalPrice;

    @Schema(description = "배송 정보")
    @JsonProperty("delivery_info")
    private DeliveryInfoDto deliveryInfo;
}
