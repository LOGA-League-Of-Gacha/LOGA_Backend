package com.loga.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.loga.domain.user.dto.TokenResponse;
import com.loga.domain.user.dto.UserResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 인증 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Auth", description = "인증 및 사용자 관련 API - Google OAuth2 로그인, JWT 토큰 관리")
public interface AuthApi {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 상세 정보를 조회합니다. 프로필, 통계, 멤버십 정보를 포함합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "user123abc",
                        "email": "summoner@gmail.com",
                        "nickname": "T1Fan",
                        "profileImage": "https://lh3.googleusercontent.com/a/photo.jpg",
                        "role": "USER",
                        "statistics": {
                          "totalGachaCount": 156,
                          "totalRosterCount": 12,
                          "championshipCount": 3,
                          "winCount": 8,
                          "loseCount": 4,
                          "winRate": 66.67
                        },
                        "membership": {
                          "isPremium": false,
                          "rerollCount": 3,
                          "premiumExpireAt": null
                        },
                        "createdAt": "2026-01-15T10:30:00"
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요 - 로그인하지 않은 사용자", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "message": "Unauthorized"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
            @Parameter(hidden = true) User user);

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다. Access Token이 만료되었을 때 사용합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwNjE4NjYwMCwiZXhwIjoxNzA2MjczMDAwfQ.xxx",
                        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwidHlwZSI6InJlZnJlc2giLCJpYXQiOjE3MDYxODY2MDAsImV4cCI6MTcwNjc5MTQwMH0.yyy",
                        "expiresIn": 86400000
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 Refresh Token", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "code": "INVALID_TOKEN",
                      "message": "Invalid or expired refresh token",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Parameter(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9...", required = true) @RequestParam String refreshToken);

    @Operation(summary = "구글 로그인 안내", description = """
            구글 소셜 로그인을 시작하기 위한 리다이렉트 URL 정보를 제공합니다.

            **로그인 흐름:**
            1. 프론트엔드에서 `/oauth2/authorization/google`로 리다이렉트
            2. Google 로그인 페이지에서 인증
            3. 인증 성공 시 프론트엔드 콜백 URL로 리다이렉트 (accessToken, refreshToken 포함)

            **콜백 URL 형식:**
            `https://league-of-gacha.pages.dev/auth/callback?accessToken=xxx&refreshToken=yyy`
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 URL 정보 반환", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": "Google 로그인을 시작하려면 /oauth2/authorization/google 로 리다이렉트하세요.",
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """)))
    })
    ResponseEntity<ApiResponse<String>> googleLoginInfo();
}
