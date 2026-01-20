package com.loga.domain.user.controller;

import com.loga.domain.user.dto.TokenResponse;
import com.loga.domain.user.dto.UserResponse;
import com.loga.domain.user.entity.User;
import com.loga.domain.user.service.AuthService;
import com.loga.global.common.dto.response.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 현재 로그인한 유저 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(authService.getCurrentUser(user)));
    }

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @RequestParam @NotBlank String refreshToken) {
        return ResponseEntity.ok(ApiResponse.success(authService.refreshToken(refreshToken)));
    }

    /**
     * Google OAuth2 로그인 안내
     */
    @GetMapping("/google")
    public ResponseEntity<ApiResponse<String>> googleLoginInfo() {
        String message = "Redirect to /oauth2/authorization/google to start Google OAuth2 login";
        return ResponseEntity.ok(ApiResponse.success("Login info", message));
    }
}
