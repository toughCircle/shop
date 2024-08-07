package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    @Schema(description = "주문 일련번호")
    @JsonProperty("order_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long orderId;

    @Schema(description = "회원 일련번호")
    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long userId;

    @Schema(description = "주문 총 금액")
    @JsonProperty("total_price")
    @Min(value = 0, message = "주문 총 금액은 0 이상이어야 합니다.")
    private int totalPrice;

    @Schema(description = "주문 상품 리스트")
    @JsonProperty("order_items")
    private List<OrderItemDto> orderItems;

    @Schema(description = "비회원 정보")
    @JsonProperty("guest_info")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private GuestDto guestInfo;

    @Schema(description = "배송 정보")
    @JsonProperty("delivery_info")
    @NotNull(message = "배송 정보는 필수입니다.")
    private DeliveryInfoDto deliveryInfo;

    @Schema(description = "배송지 정보 저장 여부")
    @JsonProperty("saveAddress_info")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private boolean saveAddressInfo;

}
