package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.Review;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
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

        return averageScore;
    }
}
