package com.loga.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 토큰 응답 DTO
 */
@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
