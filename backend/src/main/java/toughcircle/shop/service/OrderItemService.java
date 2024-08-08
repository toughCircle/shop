package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.Order;
import toughcircle.shop.model.Entity.OrderItem;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.dto.OrderItemDto;
import toughcircle.shop.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final ProductRepository productRepository;
    private final ProductService productService;

    /**
     * 주문 아이템 생성
     * @param orderItemDtoList DTO 리스트
     * @param order order 엔티티
     * @return orderItem 리스트
     */
    public List<OrderItem> createOrderItems(List<OrderItemDto> orderItemDtoList, Order order) {
        return orderItemDtoList.stream().map(orderItemDto -> {
            Product product = productRepository.findById(orderItemDto.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setTotalPrice(orderItemDto.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
    }

    /**
     * 주문 상품 DTO 변환
     * @param orderItem DTO 변환 요청 정보
     * @return 주문 상품 DTO
     */
    public OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setOrderItemId(orderItem.getId());
        dto.setProduct(productService.convertToDto(orderItem.getProduct(), false));
        dto.setQuantity(orderItem.getQuantity());
        dto.setTotalPrice(orderItem.getTotalPrice());

        return dto;
    }
}
