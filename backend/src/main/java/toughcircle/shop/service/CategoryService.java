package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Category;
import toughcircle.shop.model.dto.CategoryDto;
import toughcircle.shop.model.dto.request.AddCategoryRequest;
import toughcircle.shop.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addCategory(AddCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());

        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(CategoryDto request) throws BadRequestException {
        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new BadRequestException("Category not found with categoryId: " + request.getCategoryId()));

        category.setName(request.getName());
    }

    public List<CategoryDto> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream().map(this::convertToDto).toList();
    }

    public CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
