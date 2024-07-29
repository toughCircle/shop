package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private double calculateAverageScore(Long productId) throws BadRequestException {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product not found with productId: " + productId));

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

    @Transactional
    public void saveReview(String token, Long productId, NewReviewRequest request) throws BadRequestException {
        User user = tokenUserService.getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("Product not found with productId: " + productId));

        Review review = new Review();
        review.setUser(user);
        review.setCreateAt(LocalDateTime.now());
        review.setRating(request.getRating());
        review.setProduct(product);
        review.setComment(request.getComment());
        review.setImage(request.getImage());

        reviewRepository.save(review);

        double averageScore = calculateAverageScore(productId);
        log.info("ProductId: {}, averageScore: {}", productId, averageScore);
    }


    public ReviewListResponse getReviewList(String token, Long productId) throws BadRequestException {

        User user = tokenUserService.getUserByToken(token);
        List<Review> reviewList = reviewRepository.findByProductId(productId);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("product not found with: " + productId));

        if (reviewList.isEmpty()) {
            ReviewListResponse response = new ReviewListResponse();
            response.setMessage("Review List");
            response.setReviewList(new ArrayList<>());
            response.setAverageScore(0.0);
            return response;
        } else {
            ReviewListResponse response = new ReviewListResponse();
            response.setMessage("Review List");
            response.setReviewList(reviewList.stream().map(this::convertToDto).toList());
            response.setAverageScore(product.getAverageScore());
            return response;
        }
    }

    public ReviewDto convertToDto(Review review) {
        User user = null;
        try {
            user = userRepository.findById(review.getUser().getId())
                .orElseThrow(() -> new BadRequestException("User not found with userId: " + review.getUser().getId()));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
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


    public ReviewDto getReview(String token, Long reviewId) throws BadRequestException {
        tokenUserService.getUserByToken(token);
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new BadRequestException("Review not found with reviewId: " + reviewId));

        return convertToDto(review);
    }

    @Transactional
    public void updateReview(String token, Long reviewId, NewReviewRequest request) throws BadRequestException {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new BadRequestException("Review not found with reviewId: " + reviewId));

        review.setRating(request.getRating());
        review.setImage(request.getImage());
        review.setComment(review.getComment());
        review.setUpdateAt(LocalDateTime.now());

        reviewRepository.save(review);

        calculateAverageScore(review.getProduct().getId());
    }

    @Transactional
    public void deleteReview(String token, Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
