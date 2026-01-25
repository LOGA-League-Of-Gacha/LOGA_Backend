package com.loga.domain.roster.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.loga.infrastructure.persistence.BaseDocument;

import lombok.*;

/**
 * 로스터 도메인 엔티티 DDD Aggregate Root
 */
@Document(collection = "rosters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Roster extends BaseDocument {

    @Indexed
    private String userId;

    private String userName;

    // 5 positions - Value Object
    private RosterPlayers players;

    // Championship match info
    private ChampionshipMatch championshipMatch;

    // Community sharing
    @Builder.Default
    private CommunityInfo communityInfo = new CommunityInfo();

    // Game mode
    @Builder.Default
    private GameMode gameMode = GameMode.NORMAL;

    // Rank game specific
    private RankInfo rankInfo;

    public enum GameMode {
        NORMAL, RANKED
    }

    // ===== Value Objects =====

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RosterPlayers {
        private String topPlayerId;
        private String junglePlayerId;
        private String midPlayerId;
        private String adcPlayerId;
        private String supportPlayerId;

        // Denormalized names for quick display
        private String topPlayerName;
        private String junglePlayerName;
        private String midPlayerName;
        private String adcPlayerName;
        private String supportPlayerName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChampionshipMatch {
        private boolean isChampionshipRoster;
        private String matchedChampionship;
        private int matchedYear;

        public static ChampionshipMatch none() {
            return new ChampionshipMatch(false, null, 0);
        }

        public static ChampionshipMatch matched(String championship, int year) {
            return new ChampionshipMatch(true, championship, year);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityInfo {
        private boolean isPublic = false;
        private int likeCount = 0;
        private List<String> likedUserIds = new ArrayList<>();
        private int commentCount = 0;

        public void toggleLike(String userId) {
            if (likedUserIds.contains(userId)) {
                likedUserIds.remove(userId);
                likeCount = Math.max(0, likeCount - 1);
            } else {
                likedUserIds.add(userId);
                likeCount++;
            }
        }

        public boolean isLikedBy(String userId) {
            return likedUserIds.contains(userId);
        }

        public void incrementCommentCount() {
            this.commentCount++;
        }

        public void decrementCommentCount() {
            this.commentCount = Math.max(0, this.commentCount - 1);
        }

        public void makePublic() {
            this.isPublic = true;
        }

        public void makePrivate() {
            this.isPublic = false;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RankInfo {
        private Integer score;
        private String tier;

        public static RankInfo initial() {
            return new RankInfo(1000, "BRONZE");
        }

        public void updateScore(int delta) {
            this.score = Math.max(0, this.score + delta);
            this.tier = calculateTier(this.score);
        }

        private String calculateTier(int score) {
            if (score >= 2400)
                return "CHALLENGER";
            if (score >= 2000)
                return "MASTER";
            if (score >= 1600)
                return "DIAMOND";
            if (score >= 1400)
                return "PLATINUM";
            if (score >= 1200)
                return "GOLD";
            if (score >= 1000)
                return "SILVER";
            return "BRONZE";
        }
    }

    // ===== Factory Methods =====

    public static Roster create(String userId, String userName, RosterPlayers players,
            ChampionshipMatch championshipMatch, boolean isPublic, GameMode gameMode) {
        Roster roster = Roster.builder()
                .userId(userId)
                .userName(userName)
                .players(players)
                .championshipMatch(championshipMatch)
                .gameMode(gameMode)
                .build();

        if (isPublic) {
            roster.communityInfo.makePublic();
        }

        if (gameMode == GameMode.RANKED) {
            roster.rankInfo = RankInfo.initial();
        }

        return roster;
    }

    // ===== Domain Logic =====

    public void toggleLike(String userId) {
        this.communityInfo.toggleLike(userId);
    }

    public boolean isLikedBy(String userId) {
        return this.communityInfo.isLikedBy(userId);
    }

    public void addComment() {
        this.communityInfo.incrementCommentCount();
    }

    public void removeComment() {
        this.communityInfo.decrementCommentCount();
    }

    public void makePublic() {
        this.communityInfo.makePublic();
    }

    public void makePrivate() {
        this.communityInfo.makePrivate();
    }

    public boolean isPublic() {
        return this.communityInfo.isPublic();
    }

    public boolean isChampionshipRoster() {
        return this.championshipMatch != null && this.championshipMatch.isChampionshipRoster();
    }

    public boolean isOwnedBy(String userId) {
        return this.userId.equals(userId);
    }
}
