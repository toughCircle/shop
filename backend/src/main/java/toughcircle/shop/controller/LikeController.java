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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toughcircle.shop.model.dto.response.ErrorResponse;
import toughcircle.shop.model.dto.response.Response;

@Tag(name = "Like Controller", description = "관심상품 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/apu/likes")
public class LikeController {

    // 관심 상품 추가/해제
    @Operation(summary = "관심상품 등록/해제", description = "해당 상품을 관심상품에 등록/해제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "관심상품 둥록/해제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Response> addLike(@RequestHeader("Authorization") String token) {
        // TODO: 서비스 구현

        Response response = new Response("Product liked successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
