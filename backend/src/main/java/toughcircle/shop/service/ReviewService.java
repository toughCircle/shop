package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.Review;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.ReviewDto;
import toughcircle.shop.model.dto.request.NewReviewRequest;
import toughcircle.shop.model.dto.response.ReviewListResponse;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.ReviewRepository;
import toughcircle.shop.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final TokenUserService tokenUserService;
    private final UserRepository userRepository;

    /**
     * 상품의 평균 점수 계산
     * @param productId 상품 ID
     * @return 평균 점수
     */
    private double calculateAverageScore(Long productId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found with productId: " + productId));

        List<Review> reviewList = reviewRepository.findByProductId(productId);

        if (reviewList.isEmpty()) {
            return 0.0;
        }

        double averageScore = reviewList.stream()
            .mapToDouble(Review::getRating) // 리뷰의 점수를 추출
            .average() // 평균을 계산
            .orElse(0.0); // 리뷰가 없으면 0.0 반환

        product.setAverageScore(averageScore);
        productRepository.save(product);
        return averageScore;
    }

    /**
     * 리뷰 저장
     * @param token 사용자 토큰
     * @param productId 상품 ID
     * @param request 리뷰 요청 정보
     */
    @Transactional
    public void saveReview(String token, Long productId, NewReviewRequest request) {
        User user = tokenUserService.getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found with productId: " + productId));

        Review review = new Review();
        review.setUser(user);
        review.setCreateAt(LocalDateTime.now());
        review.setRating(request.getRating());
        review.setProduct(product);
        review.setComment(request.getComment());
        review.setImage(request.getImage());

        reviewRepository.save(review);

        double averageScore = calculateAverageScore(productId);
        log.debug("ProductId: {}, averageScore: {}", productId, averageScore);
    }

    /**
     * 특정 상품의 리뷰 리스트 조회
     * @param productId 상품 ID
     * @return 리뷰 리스트 응답
     */
    public ReviewListResponse getReviewList(Long productId) {

        List<Review> reviewList = reviewRepository.findByProductId(productId);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found with productId: " + productId));

        ReviewListResponse response = new ReviewListResponse();
        response.setMessage("Review List");
        response.setReviewList(reviewList.isEmpty() ? new ArrayList<>() : reviewList.stream().map(this::convertToDto).toList());
        response.setAverageScore(product.getAverageScore());

        return response;
    }

    /**
     * 리뷰 DTO 변환
     * @param review 리뷰 엔티티
     * @return 리뷰 DTO
     */
    public ReviewDto convertToDto(Review review) {

        User user = userRepository.findById(review.getUser().getId())
            .orElseThrow(() -> new NotFoundException("User not found with userId: " + review.getUser().getId()));

        ReviewDto dto = new ReviewDto();
        dto.setReviewId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setImage(review.getImage());
        dto.setUsername(user.getUsername());
        dto.setCreateAt(review.getCreateAt());
        dto.setUpdateAt(review.getUpdateAt());
        dto.setUserId(review.getUser().getId());
        dto.setProductId(review.getProduct().getId());

        return dto;
    }

    /**
     * 특정 리뷰 조회
     * @param reviewId 리뷰 ID
     * @return 리뷰 DTO
     */
    public ReviewDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("Review not found with reviewId: " + reviewId));

        return convertToDto(review);
    }

    /**
     * 리뷰를 업데이트합니다.
     * @param reviewId 리뷰 ID
     * @param request 리뷰 요청 정보
     */
    @Transactional
    public void updateReview(Long reviewId, NewReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("Review not found with reviewId: " + reviewId));

        review.setRating(request.getRating());
        review.setImage(request.getImage());
        review.setComment(review.getComment());
        review.setUpdateAt(LocalDateTime.now());

        reviewRepository.save(review);

        calculateAverageScore(review.getProduct().getId());
    }

    /**
     * 리뷰를 삭제합니다.
     * @param reviewId 리뷰 ID
     */
    @Transactional
    public void deleteReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);
    }
}
