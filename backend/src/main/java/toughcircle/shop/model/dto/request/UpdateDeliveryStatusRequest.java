package toughcircle.shop.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toughcircle.shop.model.Enums.DeliveryStatus;

@Data
public class UpdateDeliveryStatusRequest {

    @Schema(description = "주문 일련번호")
    @JsonProperty("order_id")
    private Long orderId;

    @Schema(description = "배송 상태")
    private DeliveryStatus status;

}
