package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class TokenUserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public User getUserByToken(String token) {
        String extractUsername = jwtUtil.extractUsername(token);

        return userRepository.findByEmail(extractUsername)
            .orElseThrow(() -> new RuntimeException("User not found whit email: " + extractUsername));
    }
}
