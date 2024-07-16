package toughcircle.shop.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import toughcircle.shop.model.dto.DeliveryInfoDto;
import toughcircle.shop.model.dto.GuestDto;
import toughcircle.shop.model.dto.OrderItemDto;

import java.util.List;

@Data
public class OrderResponse {

    @Schema(description = "주문 일련번호")
    @JsonProperty("order_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long orderId;

    @Schema(description = "회원 일련번호")
    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long userId;

    @Schema(description = "비회원 일련번호")
    @JsonProperty("guest_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long guestId;

    @Schema(description = "주문 총 금액")
    @JsonProperty("total_price")
    private int totalPrice;
    @Schema(description = "주문 상품 리스트")
    @JsonProperty("order_items")
    private List<OrderItemDto> orderItems;

    @Schema(description = "배송 정보")
    @JsonProperty("delivery_info")
    private DeliveryInfoDto deliveryInfo;
}
