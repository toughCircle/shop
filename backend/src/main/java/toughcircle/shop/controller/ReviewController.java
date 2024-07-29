package toughcircle.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toughcircle.shop.model.dto.ReviewDto;
import toughcircle.shop.model.dto.request.NewReviewRequest;
import toughcircle.shop.model.dto.response.ErrorResponse;
import toughcircle.shop.model.dto.response.Response;
import toughcircle.shop.model.dto.response.ReviewListResponse;
import toughcircle.shop.model.dto.response.ReviewResponse;
import toughcircle.shop.service.ReviewService;

import java.util.List;

@Tag(name = "Review controller", description = "상품 후기 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 상품 후기 추가
    @Operation(summary = "상품후기 추가", description = "상품후기 추가를 위한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품후기 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/products/{product_id}/reviews")
    public ResponseEntity<Response> createReview(@RequestHeader("Authorization") String token,
                                                 @PathVariable("product_id") Long productId,
                                                 @RequestBody NewReviewRequest request) throws BadRequestException {
        reviewService.saveReview(token, productId, request);

        Response response = new Response("Review added successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 모든 상품 후기 조회
    @Operation(summary = "상품후기 리스트 조회", description = "상품후기 리스트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품후기 리스트 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/products/{product_id}/reviews")
    public ResponseEntity<ReviewListResponse> getReviewList(@RequestHeader("Authorization") String token,
                                                            @PathVariable("product_id") Long productId) throws BadRequestException {
        ReviewListResponse response = reviewService.getReviewList(token, productId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 특정 상품 후기 조회
    @Operation(summary = "상품후기 조회", description = "상품후기룰 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품후기 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/reviews/{review_id}")
    public ResponseEntity<ReviewResponse> getReview(@RequestHeader("Authorization") String token,
                                                    @PathVariable("review_id") Long reviewId) throws BadRequestException {
        ReviewDto review = reviewService.getReview(token, reviewId);

        ReviewResponse response = new ReviewResponse();
        response.setMessage("Review");
        response.setReview(review);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 상품 후기 수정
    @Operation(summary = "상품후기 수정", description = "상품후기 수정을 위한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품후기 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/reviews/{review_id}")
    public ResponseEntity<Response> updateReview(@RequestHeader("Authorization") String token,
                                                       @PathVariable("review_id") Long reviewId,
                                                       @RequestBody NewReviewRequest request) throws BadRequestException {
        reviewService.updateReview(token, reviewId, request);

        Response response = new Response("Review updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 상품 후기 삭제
    @Operation(summary = "상품후기 삭제", description = "상품후기를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품후기 삭제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/reviews/{review_id}")
    public ResponseEntity<Response> deleteReview(@RequestHeader("Authorization") String token,
                                                       @PathVariable("review_id") Long reviewId) {
        reviewService.deleteReview(token, reviewId);

        Response response = new Response("Review deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
