package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Like;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.User;
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
    public void addLike(String token, Long productId) throws BadRequestException {
        User user = getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("product not found with productId: " + productId));

        Optional<Like> like = likeRepository.findByUserIdAndProductId(user.getId(), productId);
        like.ifPresent(likeRepository::delete);

        if (like.isEmpty()) {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setProduct(product);
        }

    }

    public List<Like> getLikesByUserId(Long userId) {
        return likeRepository.findByUserId(userId);
    }

    private User getUserByToken(String token) throws BadRequestException {
        String extractUsername = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }
        return user;
    }

}
