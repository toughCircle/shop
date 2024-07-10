package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.*;
import toughcircle.shop.model.dto.ProductDto;
import toughcircle.shop.model.dto.request.NewProductRequest;
import toughcircle.shop.repository.*;
import toughcircle.shop.security.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void saveProduct(String token, NewProductRequest request) throws BadRequestException {
        User user = getUserByToken(token);

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new BadRequestException("Category not found with categoryId: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setMainImage(request.getMainImageUrl());
        product.setAverageScore(0.0);

    }

    private User getUserByToken(String token) throws BadRequestException {
        String extractUsername = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }
        return user;
    }

    public List<ProductDto> getProductList(String token) throws BadRequestException {
        User user = getUserByToken(token);

        List<Product> productList = productRepository.findAll();
        List<Like> likeList = likeRepository.findByUserId(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked); // 좋아요 상태를 포함하여 DTO로 변환
        }).collect(Collectors.toList());
    }

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

    public ProductDto getProduct(String token, Long productId) throws BadRequestException {
        User user = getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product not found with productId: " + productId));

        Optional<Like> like = likeRepository.findByUserIdAndProductId(user.getId(), productId);

        return convertToDto(product, like.isPresent());
    }

    @Transactional
    public void updateProduct(String token, Long productId, NewProductRequest request) throws BadRequestException {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product not found with productId: " + productId));

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new BadRequestException("Category not found with categoryId: " + request.getCategoryId()));
        product.setName(request.getName());
        product.setCategory(category);
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setMainImage(request.getMainImageUrl());
    }

    @Transactional
    public void deleteProduct(String token, Long productId) {
        productRepository.deleteById(productId);
    }

    public List<ProductDto> findByName(String token, String query) throws BadRequestException {
        User user = getUserByToken(token);

        List<Product> productList = productRepository.findByNameContaining(query);

        List<Like> likeList = likeRepository.findByUserId(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked);
        }).collect(Collectors.toList());
    }

    public List<ProductDto> categoryFilter(String token, Long categoryId) throws BadRequestException {
        User user = getUserByToken(token);

        List<Product> productList = productRepository.findByCategory_categoryId(categoryId);

        List<Like> likeList = likeRepository.findByUserId(user.getId());
        Set<Long> likedProductIds = likeList.stream()
            .map(like -> like.getProduct().getId())
            .collect(Collectors.toSet());

        return productList.stream().map(product -> {
            boolean liked = likedProductIds.contains(product.getId());
            return convertToDto(product, liked);
        }).collect(Collectors.toList());
    }
}
