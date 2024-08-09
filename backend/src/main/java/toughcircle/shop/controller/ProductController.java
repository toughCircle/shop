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
import toughcircle.shop.model.dto.ProductDto;
import toughcircle.shop.model.dto.response.*;
import toughcircle.shop.model.dto.request.NewProductRequest;
import toughcircle.shop.service.ProductService;

import java.util.Collections;
import java.util.List;

@Tag(name = "Product Controller", description = "상품 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 상품 상세 정보 추가
    @Operation(summary = "상품 추가", description = "상품을 추가하기 위한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AddProductResponse> createProduct(
        @RequestBody NewProductRequest request) {

        Long productId = productService.saveProduct(request);

        AddProductResponse response = new AddProductResponse();
        response.setMessage("Product added successfully");
        response.setProductId(productId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 모든 상품 조회
    @Operation(summary = "상품 리스트 조회", description = "상품 리스트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> getProductList(@RequestHeader("Authorization") String token) {

        List<ProductDto> productList = productService.getProductList(token);

        ProductListResponse response = new ProductListResponse();
        response.setMessage("Product list");
        if (productList.isEmpty()) {
            response.setProductList(Collections.emptyList());
        } else {
            response.setProductList(productList);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 특정 상품 조회
    @Operation(summary = "상품 조회", description = "상품 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{product_id}")
    public ResponseEntity<ProductResponse> getProduct(@RequestHeader("Authorization") String token,
                                                      @PathVariable("product_id") Long productId) {

        ProductDto product = productService.getProduct(token, productId);

        ProductResponse response = new ProductResponse();
        response.setProduct(product);
        response.setMessage("Product");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 상품 상세 정보 수정
    @Operation(summary = "상품 정보 수정", description = "상품 상세 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{product_id}")
    public ResponseEntity<Response> updateProduct(@PathVariable("product_id") Long productId,
                                                  @RequestBody NewProductRequest request) {
        productService.updateProduct(productId, request);

        Response response = new Response("Product updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 상품 정보 삭제
    @Operation(summary = "상품 삭제", description = "상품 리스트를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 삭제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable("product_id") Long productId) {
        productService.deleteProduct(productId);

        Response response = new Response("Product deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 상품 검색
    @Operation(summary = "상품이름 검색", description = "상품 검색을 위해 이름을 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 이름 검색 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProduct(@RequestHeader("Authorization") String token,
                                                             @RequestParam("query") String query) {
        List<ProductDto> productList = productService.findByName(token, query);

        ProductListResponse response = new ProductListResponse();
        response.setMessage("Product List");
        response.setProductList(productList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 카테고리 상품 조회
    @Operation(summary = "카테고리별 상품 조회", description = "카테고리를 선택합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 이름 검색 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/categories/{category_id}")
    public ResponseEntity<ProductListResponse> categoryFilter(@RequestHeader("Authorization") String token,
                                                              @PathVariable("category_id") Long categoryId) {
        List<ProductDto> productList = productService.categoryFilter(token, categoryId);

        ProductListResponse response = new ProductListResponse();
        response.setMessage("Product List");
        response.setProductList(productList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
