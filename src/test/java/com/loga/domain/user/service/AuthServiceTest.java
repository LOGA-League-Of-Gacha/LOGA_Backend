package com.loga.domain.user.service;

import com.loga.domain.user.dto.TokenResponse;
import com.loga.domain.user.dto.UserResponse;
import com.loga.domain.user.entity.User;
import com.loga.domain.user.repository.UserRepository;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import com.loga.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 갱신 성공")
    void refreshToken_Success() {
        // given
        String refreshToken = "valid_refresh_token";
        String userId = "user1";
        String newAccess = "new_access_token";
        String newRefresh = "new_refresh_token";
        User user = User.builder()
                .email("test@example.com")
                .role(User.Role.USER)
                .build();
        user.setId(userId);

        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.isRefreshToken(refreshToken)).willReturn(true);
        given(jwtTokenProvider.getUserIdFromToken(refreshToken)).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), "USER")).willReturn(newAccess);
        given(jwtTokenProvider.createRefreshToken(user.getId())).willReturn(newRefresh);
        given(jwtTokenProvider.getExpirationTime(newAccess)).willReturn(3600000L);

        // when
        TokenResponse result = authService.refreshToken(refreshToken);

        // then
        assertThat(result.getAccessToken()).isEqualTo(newAccess);
        assertThat(result.getRefreshToken()).isEqualTo(newRefresh);
    }

    @Test
    @DisplayName("토큰 갱신 실패 - 유효하지 않은 토큰")
    void refreshToken_Fail_InvalidToken() {
        // given
        String refreshToken = "invalid_token";
        given(jwtTokenProvider.validateToken(refreshToken)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOKEN);
    }

    @Test
    @DisplayName("현재 유저 조회 성공")
    void getCurrentUser_Success() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .nickname("Tester")
                .build();

        // when
        UserResponse result = authService.getCurrentUser(user);

        // then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getNickname()).isEqualTo("Tester");
    }
}
