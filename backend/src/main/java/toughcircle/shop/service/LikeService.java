package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.Like;
import toughcircle.shop.model.Entity.Product;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.repository.LikeRepository;
import toughcircle.shop.repository.ProductRepository;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ProductService productService;

    public void addLike(String token, Long productId) throws BadRequestException {
        User user = getUserByToken(token);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BadRequestException("product not found with productId: " + productId));

        Like like = new Like();
        like.setUser(user);
        like.setProduct(product);
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
