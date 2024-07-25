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
import toughcircle.shop.model.dto.CategoryDto;
import toughcircle.shop.model.dto.request.AddCategoryRequest;
import toughcircle.shop.model.dto.response.ErrorResponse;
import toughcircle.shop.model.dto.response.Response;
import toughcircle.shop.service.CategoryService;

import java.util.List;

@Tag(name = "Category Controller", description = "카테고리 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 추가", description = "카테고리 추가를 위해 필요한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 추가 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Response> addCategory(@RequestBody AddCategoryRequest request) {
        categoryService.addCategory(request);

        Response response = new Response("Category added successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 수정을 위해 필요한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<Response> updateCategory(@RequestBody CategoryDto request) throws BadRequestException {
        categoryService.updateCategory(request);

        Response response = new Response("Category updated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 리스트 조회", description = "카테고리 리스트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 조회 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoryList() throws BadRequestException {
        List<CategoryDto> categoryList = categoryService.getCategoryList();

        return new ResponseEntity<>(categoryList, HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{category_id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable("category_id") Long categoryId) throws BadRequestException {
        categoryService.deleteCategory(categoryId);

        Response response = new Response("Category updated successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
