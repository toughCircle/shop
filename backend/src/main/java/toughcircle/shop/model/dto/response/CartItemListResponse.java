package toughcircle.shop.model.dto.response;

import lombok.Data;
import toughcircle.shop.model.dto.CartItemDto;

import java.util.List;

@Data
public class CartItemListResponse {
    List<CartItemDto> cartItemList;
}
