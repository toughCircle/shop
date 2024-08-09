package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.model.Entity.Category;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.dto.CategoryDto;
import toughcircle.shop.model.dto.request.AddCategoryRequest;
import toughcircle.shop.repository.CategoryRepository;
import toughcircle.shop.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 새로운 카테고리 추가
     * @param request 카테고리 추가 요청 정보
     */
    @Transactional
    public void addCategory(AddCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());

        categoryRepository.save(category);
    }

    /**
     * 기존 카테고리 업데이트
     * @param request 카테고리 업데이트 요청 정보
     */
    @Transactional
    public void updateCategory(CategoryDto request) {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new NotFoundException("Category not found with categoryId: " + request.getCategoryId()));

        category.setName(request.getName());
    }

    /**
     * 모든 카테고리 리스트 조회
     * @return 카테고리 DTO 리스트
     */
    public List<CategoryDto> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream().map(this::convertToDto).toList();
    }

    /**
     * 카테고리 DTO 변환
     * @param category 카테고리 엔티티
     * @return 카테고리 DTO
     */
    public CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    /**
     * 카테고리 삭제
     * @param categoryId 카테고리 ID
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));

        List<Product> products = category.getProductList();
        for (Product product : products) {
            product.setCategory(null);
            productRepository.save(product);
        }

        categoryRepository.deleteById(categoryId);
    }
}
