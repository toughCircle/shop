package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.*;
import toughcircle.shop.model.Enums.DeliveryStatus;
import toughcircle.shop.model.dto.DeliveryInfoDto;
import toughcircle.shop.model.dto.GuestDto;
import toughcircle.shop.model.dto.OrderDto;
import toughcircle.shop.model.dto.OrderItemDto;
import toughcircle.shop.model.dto.request.UpdateDeliveryStatusRequest;
import toughcircle.shop.model.dto.response.OrderListResponse;
import toughcircle.shop.model.dto.response.OrderResponse;
import toughcircle.shop.model.dto.response.OrderResultResponse;
import toughcircle.shop.repository.OrderRepository;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final TokenUserService tokenUserService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 회원 주문
    @Transactional
    public Long addOrder(OrderDto request) throws BadRequestException {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new BadRequestException("User not found with userId: " + request.getUserId()));

        String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString();

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.NEW);

        request.getOrderItems().stream().map(orderItemDto -> {
            Product product = productRepository.findById(orderItemDto.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setTotalPrice(orderItemDto.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setOrder(order);
        deliveryInfo.setOrderMemo(request.getDeliveryInfo().getOrderMemo());
        deliveryInfo.setPhone(request.getDeliveryInfo().getPhone());
        deliveryInfo.setAddress(request.getDeliveryInfo().getAddress());
        deliveryInfo.setRecipientName(request.getDeliveryInfo().getRecipientName());

        if (request.isSaveAddressInfo()) {
            user.setAddress(request.getDeliveryInfo().getAddress());
        }

        return order.getId();
    }

    // 비회원 주문
    @Transactional
    public Long addGuestOrder(OrderDto request) {
        String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString();

        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber(orderNumber);
        order.setUser(null);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.NEW);

        request.getOrderItems().stream().map(orderItemDto -> {
            Product product = productRepository.findById(orderItemDto.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setTotalPrice(orderItemDto.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        Guest guest = new Guest();
        guest.setName(request.getGuestInfo().getName());
        guest.setPhone(request.getGuestInfo().getPhone());
        guest.setEmail(request.getGuestInfo().getEmail());

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setOrder(order);
        deliveryInfo.setOrderMemo(request.getDeliveryInfo().getOrderMemo());
        deliveryInfo.setPhone(request.getDeliveryInfo().getPhone());
        deliveryInfo.setAddress(request.getDeliveryInfo().getAddress());
        deliveryInfo.setRecipientName(request.getDeliveryInfo().getRecipientName());

        return order.getId();
    }

    // 주문 결과 조회
    public OrderResultResponse getOrderResult(Long orderId) throws BadRequestException {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new BadRequestException("Order not found with orderId: " + orderId));

        OrderResultResponse response = new OrderResultResponse();

        response.setMessage("Order Result");

        if (order.getUser() != null) {
            response.setUserId(order.getUser().getId());
        }

        if (order.getGuest() != null) {
            response.setGuestId(order.getGuest().getId());
        }

        response.setTotalPrice(order.getTotalPrice());
        response.setDeliveryInfo(converToDeliveryInfoDto(order.getDeliveryInfo()));

        return response;
    }

    private DeliveryInfoDto converToDeliveryInfoDto(DeliveryInfo deliveryInfo) {
        DeliveryInfoDto dto = new DeliveryInfoDto();
        dto.setRecipientName(deliveryInfo.getRecipientName());
        dto.setAddress(deliveryInfo.getAddress());
        dto.setPhone(deliveryInfo.getPhone());
        dto.setOrderMemo(deliveryInfo.getOrderMemo());

        return dto;
    }

    public OrderResponse getOrderDetail(Long orderId) throws BadRequestException {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new BadRequestException("Order not found with orderId: " + orderId));

        return getOrderResponse(order);
    }

    private OrderResponse getOrderResponse(Order order) {

        List<OrderItemDto> orderItemList = order.getOrderItemList().stream().map(this::convertToOrderItemDto).toList();

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        if (order.getUser() != null) {
            response.setUserId(order.getUser().getId());
        }
        if (order.getGuest() != null) {
            response.setGuestId(order.getGuest().getId());
        }
        response.setOrderItems(orderItemList);
        response.setDeliveryInfo(converToDeliveryInfoDto(order.getDeliveryInfo()));
        return response;
    }

    public OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setOrderItemId(orderItem.getId());
        dto.setProduct(productService.convertToDto(orderItem.getProduct(), false));
        dto.setQuantity(orderItem.getQuantity());
        dto.setTotalPrice(orderItem.getTotalPrice());

        return dto;
    }

    public OrderListResponse getOrderList(String token) throws BadRequestException {
        User user = tokenUserService.getUserByToken(token);

        List<Order> orderList = orderRepository.findByUser_id(user.getId());

        List<OrderResponse> list = orderList.stream().map(this::getOrderResponse).toList();

        OrderListResponse response = new OrderListResponse();
        response.setOrderList(list);

        return response;
    }

    private OrderDto convertToOrderDto(Order order) {

        List<OrderItemDto> orderItemList = order.getOrderItemList().stream().map(this::convertToOrderItemDto).toList();

        OrderDto dto = new OrderDto();
        dto.setUserId(order.getUser().getId());
        dto.setGuestInfo(convertToGuestDto(order.getGuest()));
        dto.setOrderItems(orderItemList);
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderId(order.getId());
        dto.setDeliveryInfo(converToDeliveryInfoDto(order.getDeliveryInfo()));

        return dto;
    }

    private GuestDto convertToGuestDto(Guest guest) {
        GuestDto dto = new GuestDto();
        dto.setGuestId(guest.getId());
        dto.setPhone(guest.getPhone());
        dto.setName(guest.getName());
        dto.setEmail(guest.getEmail());

        return dto;
    }

    public OrderDto updateDeliveryStatus(String token, UpdateDeliveryStatusRequest request) throws BadRequestException {
        User user = tokenUserService.getUserByToken(token);

        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new BadRequestException("Order not found with id: " + request.getOrderId()));

        order.setDeliveryStatus(request.getStatus());

        return convertToOrderDto(order);
    }
}
