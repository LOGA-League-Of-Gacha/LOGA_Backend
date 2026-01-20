package com.loga.domain.player.dto;

import com.loga.domain.player.entity.Player;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 선수 응답 DTO
 */
@Getter
@Builder
public class PlayerResponse {
    private String id;
    private String name;
    private String realName;
    private String realNameKo;
    private String position;
    private String nationality;
    private String profileImage;
    private List<String> teams;
    private String currentTeam;
    private String region;
    private List<ChampionshipDto> championships;
    private List<SeasonStatsDto> seasonStats;
    private int pickedCount;
    private boolean isActive;

    @Getter
    @Builder
    public static class ChampionshipDto {
        private String tournament;
        private int year;
        private String team;
    }

    @Getter
    @Builder
    public static class SeasonStatsDto {
        private int year;
        private String team;
        private String league;
        private double kda;
        private double winRate;
        private int gamesPlayed;
        private double csPerMin;
        private double damageShare;
        private double goldShare;
    }

    public static PlayerResponse from(Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .realName(player.getRealName())
                .realNameKo(player.getRealNameKo())
                .position(player.getPosition().name())
                .nationality(player.getNationality())
                .profileImage(player.getProfileImage())
                .teams(player.getTeams())
                .currentTeam(player.getCurrentTeam())
                .region(player.getRegion())
                .championships(player.getChampionships().stream()
                        .map(c -> ChampionshipDto.builder()
                                .tournament(c.getTournament())
                                .year(c.getYear())
                                .team(c.getTeam())
                                .build())
                        .toList())
                .seasonStats(player.getSeasonStats().stream()
                        .map(s -> SeasonStatsDto.builder()
                                .year(s.getYear())
                                .team(s.getTeam())
                                .league(s.getLeague())
                                .kda(s.getKda())
                                .winRate(s.getWinRate())
                                .gamesPlayed(s.getGamesPlayed())
                                .csPerMin(s.getCsPerMin())
                                .damageShare(s.getDamageShare())
                                .goldShare(s.getGoldShare())
                                .build())
                        .toList())
                .pickedCount(player.getPickedCount())
                .isActive(player.isActive())
                .build();
    }
}
