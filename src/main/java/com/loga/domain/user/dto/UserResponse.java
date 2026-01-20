package com.loga.domain.user.dto;

import com.loga.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 응답 DTO
 */
@Getter
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;
    private String role;
    private StatisticsDto statistics;
    private MembershipDto membership;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class StatisticsDto {
        private int totalGachaCount;
        private int totalRosterCount;
        private int championshipCount;
        private int winCount;
        private int loseCount;
        private double winRate;
    }

    @Getter
    @Builder
    public static class MembershipDto {
        private boolean isPremium;
        private int rerollCount;
        private LocalDateTime premiumExpireAt;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .role(user.getRole().name())
                .statistics(StatisticsDto.builder()
                        .totalGachaCount(user.getStatistics().getTotalGachaCount())
                        .totalRosterCount(user.getStatistics().getTotalRosterCount())
                        .championshipCount(user.getStatistics().getChampionshipCount())
                        .winCount(user.getStatistics().getWinCount())
                        .loseCount(user.getStatistics().getLoseCount())
                        .winRate(user.getStatistics().getWinRate())
                        .build())
                .membership(MembershipDto.builder()
                        .isPremium(user.getMembership().isPremium())
                        .rerollCount(user.getMembership().getRerollCount())
                        .premiumExpireAt(user.getMembership().getPremiumExpireAt())
                        .build())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
