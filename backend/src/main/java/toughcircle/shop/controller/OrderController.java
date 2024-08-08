package toughcircle.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toughcircle.shop.model.dto.OrderDto;
import toughcircle.shop.model.dto.request.UpdateDeliveryStatusRequest;
import toughcircle.shop.model.dto.response.*;
import toughcircle.shop.service.OrderService;

@Tag(name = "Order Controller", description = "주문 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "회원 주문 추가", description = "회원이 주문을 위해 필요한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 주문 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AddOrderResponse> addOrder(@RequestBody OrderDto request) {

        Long orderId = orderService.addOrder(request);

        AddOrderResponse response = new AddOrderResponse();
        response.setMessage("Order added successfully");
        response.setOrderId(orderId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "비회원 주문 추가", description = "비회원이 주문을 위해 필요한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "비회원 주문 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/guest")
    public ResponseEntity<AddOrderResponse> addGuestOrder(@RequestBody OrderDto request) {

        Long orderId = orderService.addGuestOrder(request);
        AddOrderResponse response = new AddOrderResponse();
        response.setMessage("Order added successfully");
        response.setOrderId(orderId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "주문 결과 조회", description = "주문 후 완료 정보를 출력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 결과 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{order_id}")
    public ResponseEntity<OrderResultResponse> getOrderResult(@PathVariable("order_id") Long orderId) {

        OrderResultResponse response = orderService.getOrderResult(orderId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "주문 상세 조회", description = "주문 정보를 출력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{order_id}/detail")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable("order_id") Long orderId) {

        OrderResponse response = orderService.getOrderDetail(orderId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "주문 리스트 조회", description = "주문 리스트를 출력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 리스트 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<OrderListResponse> GetOrderList(@RequestHeader("Authorization") String token) {

        OrderListResponse response = orderService.getOrderList(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "주문 상태 수정", description = "주문 상태를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 상태 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/update-status")
    public ResponseEntity<OrderDto> updateDeliveryStatus(@RequestBody UpdateDeliveryStatusRequest request) {

        OrderDto response = orderService.updateDeliveryStatus(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
