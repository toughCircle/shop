package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.model.Entity.*;
import toughcircle.shop.model.Enums.DeliveryStatus;
import toughcircle.shop.model.dto.*;
import toughcircle.shop.model.dto.request.UpdateDeliveryStatusRequest;
import toughcircle.shop.model.dto.response.OrderListResponse;
import toughcircle.shop.model.dto.response.OrderResponse;
import toughcircle.shop.model.dto.response.OrderResultResponse;
import toughcircle.shop.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryInfoRepository deliveryInfoRepository;
    private final TokenUserService tokenUserService;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;
    private final DeliveryInfoService deliveryInfoService;
    private final AddressService addressService;

    /**
     * 회원 주문 저장
     * @param request 회원 주문 저장 요청 정보
     * @return 주문 ID
     */
    @Transactional
    public Long addOrder(OrderDto request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new NotFoundException("User not found with userId: " + request.getUserId()));

        String orderNumber = generateOrderNumber();

        Order order = createUserOrder(user, orderNumber);

        List<OrderItem> orderItems = orderItemService
            .createOrderItems(request.getOrderItems(), order);
        DeliveryInfo deliveryInfo = deliveryInfoService
            .createDeliveryInfo(request.getDeliveryInfo(), order);

        if (request.isSaveAddressInfo()) {
            addressService.saveAddressInfo(user, request.getDeliveryInfo().getAddress());
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        deliveryInfoRepository.save(deliveryInfo);

        return order.getId();
    }

    /**
     * 주문 번호 생성
     * @return 주문 번호
     */
    private String generateOrderNumber() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + "-" + UUID.randomUUID();
    }

    /**
     * 회원 주문 생성
     * @param user 회원 엔티티
     * @param orderNumber 주문 번호
     * @return 주문 정보
     */
    private Order createUserOrder(User user, String orderNumber) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setDeliveryStatus(DeliveryStatus.NEW);
        return order;
    }

    /**
     * 비회원 주문 저장
     * @param request 비회원 주문 저장 요청 정보
     * @return 주문 ID
     */
    @Transactional
    public Long addGuestOrder(OrderDto request) {

        String orderNumber = generateOrderNumber();

        Order order = createGuestOrder(orderNumber);

        List<OrderItem> orderItems = orderItemService.createOrderItems(request.getOrderItems(), order);

        Guest guest = new Guest();
        guest.setName(request.getGuestInfo().getName());
        guest.setPhone(request.getGuestInfo().getPhone());
        guest.setEmail(request.getGuestInfo().getEmail());

        DeliveryInfo deliveryInfo = deliveryInfoService
            .createDeliveryInfo(request.getDeliveryInfo(), order);

        deliveryInfoRepository.save(deliveryInfo);
        orderItemRepository.saveAll(orderItems);
        guestRepository.save(guest);

        return order.getId();
    }

    /**
     * 비회원 주문 생성
     * @param orderNumber 주문 번호
     * @return order 엔티티
     */
    private static Order createGuestOrder(String orderNumber) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderNumber(orderNumber);
        order.setUser(null);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryStatus(DeliveryStatus.NEW);
        return order;
    }

    /**
     * 주문 결과 조회
     * @param orderId 주문 ID
     * @return 주문 결과 응답 DTO [주문 상품 리스트 미포함]
     */
    public OrderResultResponse getOrderResult(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found with orderId: " + orderId));

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

    /**
     * 배송 정보 DTO 변환
     * @param deliveryInfo deliveryInfo 엔티티
     * @return 배송 정보 DTO
     */
    private DeliveryInfoDto converToDeliveryInfoDto(DeliveryInfo deliveryInfo) {
        AddressDto addressDto = new AddressDto();
        addressDto.setZipCode(deliveryInfo.getAddress().getZipCode());
        addressDto.setStreetAddress(deliveryInfo.getAddress().getStreetAddress());
        addressDto.setAddressDetail(deliveryInfo.getAddress().getAddressDetail());
        addressDto.setAddressType(deliveryInfo.getAddress().getAddressType());

        DeliveryInfoDto dto = new DeliveryInfoDto();
        dto.setRecipientName(deliveryInfo.getRecipientName());
        dto.setAddress(addressDto);
        dto.setPhone(deliveryInfo.getPhone());
        dto.setOrderMemo(deliveryInfo.getOrderMemo());

        return dto;
    }

    /**
     * 주문 상세 내역 조회
     * @param orderId 주문 ID
     * @return 주문 결과 [주문 상품 리스트 포함]
     */
    public OrderResponse getOrderDetail(Long orderId) {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found with orderId: " + orderId));

        return getOrderResponse(order);
    }

    /**
     * 주문 응답값 변환
     * @param order 변환 요청 정보 [주문 정보]
     * @return 주문 응답 DTO
     */
    private OrderResponse getOrderResponse(Order order) {

        List<OrderItemDto> orderItemList = order.getOrderItemList()
            .stream().map(orderItemService::convertToOrderItemDto)
            .toList();

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



    /**
     * 주문 내역 조회
     * @param token 사용자 토큰
     * @return 주문 내역 정보
     */
    public OrderListResponse getOrderList(String token) {
        User user = tokenUserService.getUserByToken(token);

        List<Order> orderList = orderRepository.findByUser_id(user.getId());

        List<OrderResponse> list = orderList.stream().map(this::getOrderResponse).toList();

        OrderListResponse response = new OrderListResponse();
        response.setOrderList(list);

        return response;
    }

    /**
     * 주문 DTO 변환
     * @param order order 엔티티
     * @return 주문 DTO
     */
    private OrderDto convertToOrderDto(Order order) {

        List<OrderItemDto> orderItemList = order.getOrderItemList()
            .stream().map(orderItemService::convertToOrderItemDto)
            .toList();

        OrderDto dto = new OrderDto();
        dto.setUserId(order.getUser().getId());
        dto.setGuestInfo(convertToGuestDto(order.getGuest()));
        dto.setOrderItems(orderItemList);
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderId(order.getId());
        dto.setDeliveryInfo(converToDeliveryInfoDto(order.getDeliveryInfo()));

        return dto;
    }

    /**
     * 비회원 DTO 변환
     * @param guest guest 엔티티
     * @return 비회원 DTO
     */
    private GuestDto convertToGuestDto(Guest guest) {
        GuestDto dto = new GuestDto();
        dto.setGuestId(guest.getId());
        dto.setPhone(guest.getPhone());
        dto.setName(guest.getName());
        dto.setEmail(guest.getEmail());

        return dto;
    }

    /**
     * 상품 배송 상태 수정
     * @param request 배송 상태 수정 요청 정보
     * @return 주문 정보
     */
    public OrderDto updateDeliveryStatus(UpdateDeliveryStatusRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new NotFoundException("Order not found with id: " + request.getOrderId()));

        order.setDeliveryStatus(request.getStatus());

        return convertToOrderDto(order);
    }
}
