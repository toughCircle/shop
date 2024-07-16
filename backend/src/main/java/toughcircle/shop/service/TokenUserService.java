package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class TokenUserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public User getUserByToken(String token) throws BadRequestException {
        String extractUsername = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }
        return user;
    }
}
