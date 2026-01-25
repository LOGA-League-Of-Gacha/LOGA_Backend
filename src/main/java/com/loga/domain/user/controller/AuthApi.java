package com.loga.domain.user.controller;

import com.loga.domain.user.dto.TokenResponse;
import com.loga.domain.user.dto.UserResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth", description = "인증 및 사용자 관련 API")
public interface AuthApi {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 상세 정보를 조회합니다.")
    ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @Parameter(hidden = true) User user);

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.")
    ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @RequestParam String refreshToken);

    @Operation(summary = "구글 로그인 안내", description = "구글 소셜 로그인을 시작하기 위한 리다이렉트 URL 정보를 제공합니다.")
    ResponseEntity<ApiResponse<String>> googleLoginInfo();
}
