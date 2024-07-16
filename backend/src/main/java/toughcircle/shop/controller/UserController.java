package toughcircle.shop.controller;

import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toughcircle.shop.model.dto.LoginUserDto;
import toughcircle.shop.model.dto.UserDto;
import toughcircle.shop.model.dto.request.LoginRequest;
import toughcircle.shop.model.dto.request.RegisterRequest;
import toughcircle.shop.model.dto.request.ResetPasswordRequest;
import toughcircle.shop.model.dto.request.UpdateUserRequest;
import toughcircle.shop.model.dto.response.*;
import toughcircle.shop.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "User Controller", description = "사용자 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 위해 필수 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@Valid @RequestBody RegisterRequest request) throws AlreadyUsedException {

        userService.saveUser(request);

        Response response = new Response("User registered successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 로그인
    @Operation(summary = "로그인", description = "사용자가 로그인을 위해 필수 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws BadRequestException {

        LoginUserDto userDto = userService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", userDto.getAccessToken());
        headers.add("RefreshToken", userDto.getRefreshToken());

        LoginResponse response = new LoginResponse("User login successfully", userDto.getUser().getUserId());
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    // 비밀번호 찾기 (메일 전송)
    @Operation(summary = "비밀번호 찾기", description = "사용자가 비밀번호를 재설정하기 위해 이메일 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 메일 전송 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Response> forgotPassword(@RequestBody ResetPasswordRequest request) throws BadRequestException {

        userService.forgetPassword(request);

        Response response = new Response("Password reset link sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 비밀번호 재설정
    @Operation(summary = "비밀번호 재설정", description = "사용자가 비밀번호를 재설정하기 위해 새로운 비밀번호를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "비밀번호 재설정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordRequest request) throws BadRequestException {

        userService.resetPassword(request);

        Response response = new Response("Reset password successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 사용자 정보 조회
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정보 조회 성공",
            content = @Content(schema = @Schema(implementation = UserInfoResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUser(@RequestHeader("Authorization") String token) throws BadRequestException {

        UserDto user = userService.getUser(token);

        UserInfoResponse response = new UserInfoResponse("User info check successfully", user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 사용자 정보 수정
    @Operation(summary = "사용자 정보 수정", description = "사용자가 수정하기 위한 정보를 입력합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정보 수정 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping
    public ResponseEntity<Response> updateUser(@RequestHeader("Authorization") String token,
                                               @RequestBody UpdateUserRequest request) throws BadRequestException {

        userService.updateUser(token, request);

        Response response = new Response("User info updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 탈퇴
    @Operation(summary = "탈퇴", description = "시용자 정보를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "탈퇴 성공",
            content = @Content(schema = @Schema(implementation = Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<Response> deleteUser(@RequestHeader("Authorization") String token) throws BadRequestException {

        userService.deleteUser(token);

        Response response = new Response("User info deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
