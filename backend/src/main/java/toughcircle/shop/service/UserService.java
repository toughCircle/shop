package toughcircle.shop.service;

import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Cart;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.LoginUserDto;
import toughcircle.shop.model.dto.UserDto;
import toughcircle.shop.model.dto.request.LoginRequest;
import toughcircle.shop.model.dto.request.RegisterRequest;
import toughcircle.shop.model.dto.request.ResetPasswordRequest;
import toughcircle.shop.model.dto.request.UpdateUserRequest;
import toughcircle.shop.repository.CartRepository;
import toughcircle.shop.repository.UserRepository;
import toughcircle.shop.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    // 사용자 정보 저장
    @Transactional
    public void saveUser(RegisterRequest request) throws AlreadyUsedException {

        User userByEmail = userRepository.findByEmail(request.getEmail());
        if (userByEmail != null) {
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

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.info(encodedPassword);

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

    // 비밀번호 재설정 메일 전송
    public void forgetPassword(ResetPasswordRequest request) throws BadRequestException {

        User userByEmail = userRepository.findByEmail(request.getEmail());

        if (userByEmail == null) {
            throw new BadRequestException("Invalid Email");
        }

        String code = UUID.randomUUID().toString();
        log.info("Reset password code: {}",code);

        redisService.save(code, request.getEmail(), 30, TimeUnit.MINUTES);

        String resetLink = "http://localhost:3000/reset-password?code=" + code;
        String htmlContent = "<html><body><p>안녕하세요,</p><p>비밀번호를 재설정하려면 아래 링크를 클릭하세요:</p>" +
            "<a href='"+ resetLink + "'>비밀번호 재설정</a><p>링크는 30분 후에 먼료됩니다.</p></body></html>";

        mailService.sendMail(request.getEmail(), "SHOP SERVICE 비밀번호 재설정", htmlContent);
    }
    

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(ResetPasswordRequest request) throws BadRequestException {

        String email = redisService.find(request.getCode());

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new BadRequestException("Invalid code.");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encodedPassword);
    }

    public UserDto getUser(String token) throws BadRequestException {

        String extractUsername = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }

        return convertToDto(user);
    }

    @Transactional
    public void updateUser(String token, UpdateUserRequest request) throws BadRequestException {

        String extractUsername = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }

        LoginUserDto response = new LoginUserDto();

        if (request.getOldPassword() != null && request.getNewPassword() != null) {
            // 비밀번호 확인
            boolean matches = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
            if (!matches) {
                throw new BadRequestException("Invalid Password");
            }
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());

            user.setPassword(encodedPassword);
        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
    }

    public void deleteUser(String token) throws BadRequestException {

        String extractUsername = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(extractUsername);
        if (user == null) {
            throw new BadRequestException("User not found with email: " + extractUsername);
        }
        userRepository.delete(user);
    }
}
