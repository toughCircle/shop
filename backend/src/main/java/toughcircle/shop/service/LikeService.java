package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Like;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.response.Response;
import toughcircle.shop.repository.LikeRepository;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

import java.util.List;
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

    @Transactional
    public Response addLike(String token, Long productId) throws BadRequestException {
        User user = getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("product not found with productId: " + productId));

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

    public List<Like> getLikesByUserId(Long userId) {
        return likeRepository.findByUser_id(userId);
    }

    private User getUserByToken(String token) throws BadRequestException {
        String extractUsername = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(extractUsername)
            .orElseThrow(() -> new RuntimeException("User not found whit email: " + extractUsername));

        return user;
    }

}
