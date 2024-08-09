package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.exception.exceptions.NotFoundException;
import toughcircle.shop.exception.exceptions.UnauthorizedException;
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
    private final AddressService addressService;

    /**
     * 사용자 생성
     * @param request 회원가입 요청 정보
     */
    @Transactional
    public void createUser(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        // 회원가입 시 해당 사용자의 장바구니 생성
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
        userDto.setAddressList(user.getAddressList());
        userDto.setPhone(user.getPhone());

        return userDto;
    }

    /**
     * 로그인
     * @param request 로그인 요청 정보
     * @return id, token 정보
     */
    public LoginUserDto login(LoginRequest request){

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        log.info(encodedPassword);

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));


        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            throw new UnauthorizedException("Invalid Password");
        }

        String accessToken = jwtUtil.generateToken(request.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        LoginUserDto response = new LoginUserDto();
        response.setUser(convertToDto(user));
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    /**
     * 비밀번호 재설정 메일 요청
     * @param request 비밀번호 재설정 메일 요청 정보 [email]
     */
    public void forgetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new NotFoundException("User not found with email: " + request.getEmail()));

        String code = UUID.randomUUID().toString();
        log.debug("Reset password code: {}, email: {}", code, user.getEmail());

        redisService.save(code, request.getEmail(), 30, TimeUnit.MINUTES);

        String resetLink = "http://localhost:3000/reset-password?code=" + code;
        String htmlContent = "<html><body><p>안녕하세요,</p><p>비밀번호를 재설정하려면 아래 링크를 클릭하세요:</p>" +
            "<a href='"+ resetLink + "'>비밀번호 재설정</a><p>링크는 30분 후에 먼료됩니다.</p></body></html>";

        mailService.sendMail(request.getEmail(), "SHOP SERVICE 비밀번호 재설정", htmlContent);
    }

    /**
     * 비밀번호 재설정
     * @param request 비밀번호 재설정 요청 정보 [이전 비밀번호, 새로운 비밀번호]
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        String email = redisService.find(request.getCode());

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with code: " + request.getCode()));

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(encodedPassword);
    }

    /**
     * 사용자 정보 조회
     * @param token 사용자 토큰
     * @return UserDto 사용자 정보
     */
    public UserDto getUserInfo(String token) {

        String extractUsername = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(extractUsername)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + extractUsername));

        return convertToDto(user);
    }

    /**
     * 사용자 정보 수정
     * @param token 사용자 토큰
     * @param request 수정 요청 정보
     */
    @Transactional
    public void updateUser(String token, UpdateUserRequest request) {

        String username = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + username));

        // 비밀번호 수정 정보가 포함되어있을 경우 비밀번호 수정
        if (request.getOldPassword() != null && request.getNewPassword() != null) {
            // 비밀번호 확인
            boolean matches = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
            if (!matches) {
                throw new UnauthorizedException("Invalid Password");
            }

            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(encodedPassword);
        }

        // 주소 수정 정보가 포함되어있을 경우
        if (request.getAddress() != null) {
            addressService.saveAddressInfo(user, request.getAddress());
        }

        userRepository.save(user);
    }

    /**
     * 사용자 정보 삭제
     * @param token 사용자 토큰
     */
    public void deleteUser(String token) {

        String extractUsername = jwtUtil.extractUsername(token);

        User user = userRepository.findByEmail(extractUsername)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + extractUsername));

        userRepository.delete(user);
    }
}
