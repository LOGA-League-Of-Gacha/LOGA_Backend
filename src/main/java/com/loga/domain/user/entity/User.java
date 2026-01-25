package com.loga.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.loga.infrastructure.persistence.BaseDocument;

import lombok.*;

/**
 * 사용자 도메인 엔티티 DDD Aggregate Root
 */
@Document(collection = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseDocument {

    @Indexed(unique = true)
    private String email;

    private String nickname;

    private String profileImage;

    private String provider;

    private String providerId;

    @Builder.Default
    private Role role = Role.USER;

    @Builder.Default
    private List<String> rosterIds = new ArrayList<>();

    // Statistics
    @Builder.Default
    private UserStatistics statistics = new UserStatistics();

    // Membership
    @Builder.Default
    private MembershipInfo membership = new MembershipInfo();

    public enum Role {
        USER, ADMIN
    }

    // ===== Factory Methods =====

    /**
     * OAuth2 사용자 생성
     */
    public static User createOAuth2User(String provider, String providerId, String email, String nickname,
            String profileImage) {
        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .nickname(nickname)
                .profileImage(profileImage)
                .role(Role.USER)
                .statistics(new UserStatistics())
                .membership(new MembershipInfo())
                .build();
    }

    // ===== Domain Logic =====

    /**
     * 프로필 업데이트
     */
    public void updateProfile(String nickname, String profileImage) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileImage != null) {
            this.profileImage = profileImage;
        }
    }

    /**
     * 가챠 횟수 증가
     */
    public void incrementGachaCount(int count) {
        this.statistics.addGachaCount(count);
    }

    /**
     * 로스터 추가
     */
    public void addRoster(String rosterId, boolean isChampionship) {
        this.rosterIds.add(rosterId);
        this.statistics.addRosterCount();
        if (isChampionship) {
            this.statistics.addChampionshipCount();
        }
    }

    /**
     * 로스터 제거
     */
    public void removeRoster(String rosterId) {
        this.rosterIds.remove(rosterId);
        this.statistics.decrementRosterCount();
    }

    /**
     * 리롤 사용
     */
    public boolean useReroll() {
        if (membership.isPremium()) {
            return true; // 프리미엄은 무제한
        }
        return membership.useReroll();
    }

    /**
     * 리롤 가능 여부
     */
    public boolean canReroll() {
        return membership.isPremium() || membership.getRerollCount() > 0;
    }

    /**
     * 프리미엄 활성화
     */
    public void activatePremium(LocalDateTime expireAt) {
        this.membership.activatePremium(expireAt);
    }

    /**
     * 승/패 기록
     */
    public void recordGameResult(boolean isWin) {
        this.statistics.recordGame(isWin);
    }

    // ===== Value Objects =====

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatistics {
        private int totalGachaCount = 0;
        private int totalRosterCount = 0;
        private int championshipCount = 0;
        private int winCount = 0;
        private int loseCount = 0;

        public void addGachaCount(int count) {
            this.totalGachaCount += count;
        }

        public void addRosterCount() {
            this.totalRosterCount++;
        }

        public void decrementRosterCount() {
            if (this.totalRosterCount > 0) {
                this.totalRosterCount--;
            }
        }

        public void addChampionshipCount() {
            this.championshipCount++;
        }

        public void recordGame(boolean isWin) {
            if (isWin) {
                this.winCount++;
            } else {
                this.loseCount++;
            }
        }

        public double getWinRate() {
            int total = winCount + loseCount;
            return total > 0 ? (double) winCount / total * 100 : 0;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MembershipInfo {
        private boolean isPremium = false;
        private LocalDateTime premiumExpireAt;
        private int rerollCount = 3;

        public boolean useReroll() {
            if (rerollCount > 0) {
                rerollCount--;
                return true;
            }
            return false;
        }

        public void activatePremium(LocalDateTime expireAt) {
            this.isPremium = true;
            this.premiumExpireAt = expireAt;
        }

        public boolean isPremium() {
            if (!isPremium)
                return false;
            if (premiumExpireAt == null)
                return true;
            return LocalDateTime.now().isBefore(premiumExpireAt);
        }
    }
}
