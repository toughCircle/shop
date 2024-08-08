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
import toughcircle.shop.model.dto.response.ErrorResponse;
import toughcircle.shop.model.dto.response.Response;
import toughcircle.shop.service.LikeService;

@Tag(name = "Like Controller", description = "관심상품 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    // 관심 상품 추가/해제
    @Operation(summary = "관심상품 등록/해제", description = "해당 상품을 관심상품에 등록/해제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "관심상품 둥록/해제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{product_id}")
    public ResponseEntity<Response> addLike(@RequestHeader("Authorization") String token,
                                            @PathVariable("product_id") Long productId) {

        Response response = likeService.addLike(token, productId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
