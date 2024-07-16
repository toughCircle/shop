package toughcircle.shop.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderListResponse {
    private List<OrderResponse> orderList;
}
