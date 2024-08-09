package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.model.Entity.Like;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.response.Response;
import toughcircle.shop.repository.LikeRepository;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 상품에 좋아요 추가 / 제거
     * @param token 사용자 토큰
     * @param productId 상품 ID
     * @return 처리 결과 응답 메시지
     */
    @Transactional
    public Response addLike(String token, Long productId) {
        User user = getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("product not found with productId: " + productId));

        Optional<Like> like = likeRepository.findByUser_idAndProduct_id(user.getId(), productId);

        Response response = new Response();

        if (like.isEmpty()) {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setProduct(product);

            likeRepository.save(newLike);
            response.setMessage("Product liked successfully");
        } else {
            likeRepository.delete(like.get());

            response.setMessage("Product unliked successfully");
        }

        return response;
    }

    /**
     * 토큰을 기반으로 사용자 정보 조회
     * @param token 사용자 토큰
     * @return 사용자 엔티티
     */
    private User getUserByToken(String token) {
        String extractUsername = jwtUtil.extractUsername(token);

        return userRepository.findByEmail(extractUsername)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + extractUsername));
    }

}
