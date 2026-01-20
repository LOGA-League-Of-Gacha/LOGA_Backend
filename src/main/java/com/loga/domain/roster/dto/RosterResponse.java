package com.loga.domain.roster.dto;

import com.loga.domain.roster.entity.Roster;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 로스터 응답 DTO
 */
@Getter
@Builder
public class RosterResponse {

    private String id;
    private String userId;
    private String userName;
    private PlayersDto players;
    private ChampionshipMatchDto championshipMatch;
    private CommunityDto community;
    private String gameMode;
    private RankDto rank;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class PlayersDto {

        private String topPlayerId;
        private String junglePlayerId;
        private String midPlayerId;
        private String adcPlayerId;
        private String supportPlayerId;
        private String topPlayerName;
        private String junglePlayerName;
        private String midPlayerName;
        private String adcPlayerName;
        private String supportPlayerName;
    }

    @Getter
    @Builder
    public static class ChampionshipMatchDto {

        private boolean isChampionshipRoster;
        private String matchedChampionship;
        private int matchedYear;
    }

    @Getter
    @Builder
    public static class CommunityDto {

        private boolean isPublic;
        private int likeCount;
        private int commentCount;
    }

    @Getter
    @Builder
    public static class RankDto {

        private Integer score;
        private String tier;
    }

    public static RosterResponse from(Roster roster) {
        return RosterResponse.builder()
                             .id(roster.getId())
                             .userId(roster.getUserId())
                             .userName(roster.getUserName())
                             .players(PlayersDto.builder()
                                                .topPlayerId(roster.getPlayers()
                                                                   .getTopPlayerId())
                                                .junglePlayerId(roster.getPlayers()
                                                                      .getJunglePlayerId())
                                                .midPlayerId(roster.getPlayers()
                                                                   .getMidPlayerId())
                                                .adcPlayerId(roster.getPlayers()
                                                                   .getAdcPlayerId())
                                                .supportPlayerId(roster.getPlayers()
                                                                       .getSupportPlayerId())
                                                .topPlayerName(roster.getPlayers()
                                                                     .getTopPlayerName())
                                                .junglePlayerName(roster.getPlayers()
                                                                        .getJunglePlayerName())
                                                .midPlayerName(roster.getPlayers()
                                                                     .getMidPlayerName())
                                                .adcPlayerName(roster.getPlayers()
                                                                     .getAdcPlayerName())
                                                .supportPlayerName(roster.getPlayers()
                                                                         .getSupportPlayerName())
                                                .build())
                             .championshipMatch(ChampionshipMatchDto.builder()
                                                                    .isChampionshipRoster(roster.getChampionshipMatch()
                                                                                                .isChampionshipRoster())
                                                                    .matchedChampionship(roster.getChampionshipMatch()
                                                                                               .getMatchedChampionship())
                                                                    .matchedYear(roster.getChampionshipMatch()
                                                                                       .getMatchedYear())
                                                                    .build())
                             .community(CommunityDto.builder()
                                                    .isPublic(roster.getCommunityInfo()
                                                                    .isPublic())
                                                    .likeCount(roster.getCommunityInfo()
                                                                     .getLikeCount())
                                                    .commentCount(roster.getCommunityInfo()
                                                                        .getCommentCount())
                                                    .build())
                             .gameMode(roster.getGameMode()
                                             .name())
                             .rank(roster.getRankInfo() != null ? RankDto.builder()
                                                                         .score(roster.getRankInfo()
                                                                                      .getScore())
                                                                         .tier(roster.getRankInfo()
                                                                                     .getTier())
                                                                         .build() : null)
                             .createdAt(roster.getCreatedAt())
                             .build();
    }
}
