package toughcircle.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toughcircle.shop.model.dto.request.AddCartItemRequest;
import toughcircle.shop.model.dto.request.NewProductRequest;
import toughcircle.shop.model.dto.response.ErrorResponse;
import toughcircle.shop.model.dto.response.Response;

@Tag(name = "Cart controller", description = "장바구니 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    // 장바구니에 상품 추가
    @Operation(summary = "장바구니에 상품 추가", description = "해당 상품을 장바구니에 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "장바구니에 상품 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{cart_id}/items")
    public ResponseEntity<Response> addCartItem(@RequestHeader("Authorization") String token,
                                                @PathVariable("cart_id") Long cartId,
                                                @RequestBody AddCartItemRequest request) {
        // TODO: 서비스 구현

        Response response = new Response("Item added to cart successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 장바구니 상품 수량 수정
    @Operation(summary = "장바구니 상품 수량 수정", description = "장바구니의 상품 수량을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "장바구니 상품 수량 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{cart_id}/items/{item_id}")
    public ResponseEntity<Response> updateQuantity(@RequestHeader("Authorization") String token,
                                                   @PathVariable("cart_id") Long cartId,
                                                   @PathVariable("item_id") Long cartItemId,
                                                   @RequestBody AddCartItemRequest request) {
        // TODO: 서비스 구현

        Response response = new Response("Cart item updated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 장바구니 상품 삭제
    @Operation(summary = "장바구니 상품 삭제", description = "장바구니의 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "장바구니 상품 삭제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{cart_id}/items/{item_id}")
    public ResponseEntity<Response> deleteItem(@RequestHeader("Authorization") String token,
                                               @PathVariable("cart_id") Long cartId,
                                               @PathVariable("item_id") Long cartItemId,
                                               @RequestBody AddCartItemRequest request) {
        // TODO: 서비스 구현

        Response response = new Response("Cart item deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
