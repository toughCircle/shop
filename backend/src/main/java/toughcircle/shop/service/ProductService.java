package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.*;
import toughcircle.shop.model.dto.ProductDto;
import toughcircle.shop.model.dto.request.NewProductRequest;
import toughcircle.shop.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final TokenUserService tokenUserService;

    /**
     * 새로운 상품 저장
     * @param request 상품 요청 정보
     * @return 저장된 상품의 ID
     */
    @Transactional
    public Long saveProduct(NewProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with categoryId: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setMainImage(request.getMainImageUrl());
        product.setAverageScore(0.0);

        productRepository.save(product);

        return product.getId();
    }

    /**
     * 사용자 토큰을 기반으로 상품 리스트 조회
     * @param token 사용자 토큰
     * @return 상품 DTO 리스트
     */
    public List<ProductDto> getProductList(String token) {
        User user = tokenUserService.getUserByToken(token);

        List<Product> productList = productRepository.findAll();
        List<Like> likeList = likeRepository.findByUser_id(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked); // 좋아요 상태를 포함하여 DTO로 변환
        }).collect(Collectors.toList());
    }

    /**
     * 상품 DTO 변환
     * @param product 상품 엔티티
     * @param liked 사용자가 좋아요를 눌렀는지 여부
     * @return 상품 DTO
     */
    public ProductDto convertToDto(Product product, boolean liked) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        productDto.setCategoryId(product.getCategory().getId());
        productDto.setMainImageUrl(product.getMainImage());
        productDto.setAverageScore(product.getAverageScore());
        productDto.setLiked(liked);

        return productDto;
    }

    /**
     * 특정 상품 조회
     * @param token 사용자 토큰
     * @param productId 상품 ID
     * @return 상품 DTO
     */
    public ProductDto getProduct(String token, Long productId) {
        User user = tokenUserService.getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with productId: " + productId));

        Optional<Like> like = likeRepository.findByUser_idAndProduct_id(user.getId(), productId);

        return convertToDto(product, like.isPresent());
    }

    /**
     * 상품 정보 업데이트
     * @param productId 상품 ID
     * @param request 업데이트할 상품 정보
     */
    @Transactional
    public void updateProduct(Long productId, NewProductRequest request) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with productId: " + productId));

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with categoryId: " + request.getCategoryId()));
            product.setCategory(category);
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());

        if (request.getMainImageUrl() != null) {
            product.setMainImage(request.getMainImageUrl());
        }
        productRepository.save(product);
    }

    /**
     * 상품 삭제
     * @param productId 상품 ID
     */
    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    /**
     * 이름으로 상품 검색
     * @param token 사용자 토큰
     * @param query 검색어
     * @return 검색된 상품 DTO 리스트
     */
    public List<ProductDto> findByName(String token, String query) {
        User user = tokenUserService.getUserByToken(token);

        String trimmedQuery = query.trim();

        List<Product> productList = productRepository.findByNameContainingIgnoreCase(trimmedQuery);

        List<Like> likeList = likeRepository.findByUser_id(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked);
        }).collect(Collectors.toList());
    }

    /**
     * 카테고리로 상품 필터링
     * @param token 사용자 토큰
     * @param categoryId 카테고리 ID
     * @return 필터링된 상품 DTO 리스트
     */
    public List<ProductDto> categoryFilter(String token, Long categoryId) {
        User user = tokenUserService.getUserByToken(token);

        List<Product> productList = productRepository.findByCategory_id(categoryId);

        List<Like> likeList = likeRepository.findByUser_id(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked);
        }).collect(Collectors.toList());
    }
}
