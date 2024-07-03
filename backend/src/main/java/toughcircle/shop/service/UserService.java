package toughcircle.shop.service;

import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.Cart;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.LoginUserDto;
import toughcircle.shop.model.dto.UserDto;
import toughcircle.shop.model.dto.request.LoginRequest;
import toughcircle.shop.model.dto.request.RegisterRequest;
import toughcircle.shop.repository.CartRepository;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtUtil jwtUtil;

    // 사용자 정보 저장
    public void saveUser(RegisterRequest request) throws AlreadyUsedException {

        User user1 = userRepository.findByEmail(request.getEmail());
        if (user1 != null) {
            throw new AlreadyUsedException("A email that already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        // 장바구니 생성
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreateAt(LocalDateTime.now());

        cartRepository.save(cart);

        log.info(convertToDto(user).toString());
    }

    private static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setPhone(user.getPhone());

        return userDto;
    }

    // 로그인
    public LoginUserDto login(LoginRequest request) throws BadRequestException {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new BadRequestException("User not found");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!matches) {
            throw new BadRequestException("Invalid Password");
        }

        String accessToken = jwtUtil.generateToken(request.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        LoginUserDto response = new LoginUserDto();
        response.setUser(convertToDto(user));
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
