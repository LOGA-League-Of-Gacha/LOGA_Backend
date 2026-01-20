package com.loga.domain.player.entity;

import com.loga.infrastructure.persistence.BaseDocument;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 선수 도메인 엔티티
 * DDD Aggregate Root
 */
@Document(collection = "players")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Player extends BaseDocument {

    @Indexed
    private String name;

    private String realName;

    private String realNameKo;

    @Indexed
    private Position position;

    private String nationality;

    private String profileImage;

    @Builder.Default
    private List<String> teams = new ArrayList<>();

    private String currentTeam;

    @Indexed
    private String region;

    @Builder.Default
    private List<Championship> championships = new ArrayList<>();

    @Builder.Default
    private List<SeasonStats> seasonStats = new ArrayList<>();

    @Builder.Default
    private int pickedCount = 0;

    private boolean isActive;

    public enum Position {
        TOP, JUNGLE, MID, ADC, SUPPORT
    }

    // ===== Value Objects =====

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Championship {
        private String tournament;
        private int year;
        private String team;

        public String getDisplayName() {
            return year + " " + tournament + " - " + team;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeasonStats {
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

    // ===== Factory Methods =====

    public static Player create(String name, String realName, String realNameKo,
                                 Position position, String nationality, String region) {
        return Player.builder()
                .name(name)
                .realName(realName)
                .realNameKo(realNameKo)
                .position(position)
                .nationality(nationality)
                .region(region)
                .isActive(true)
                .build();
    }

    // ===== Domain Logic =====

    /**
     * 가챠에서 뽑힘 (카운트 증가)
     */
    public void picked() {
        this.pickedCount++;
    }

    /**
     * 팀 변경
     */
    public void changeTeam(String newTeam) {
        if (this.currentTeam != null && !this.teams.contains(this.currentTeam)) {
            this.teams.add(this.currentTeam);
        }
        this.currentTeam = newTeam;
    }

    /**
     * 우승 기록 추가
     */
    public void addChampionship(String tournament, int year, String team) {
        this.championships.add(Championship.builder()
                .tournament(tournament)
                .year(year)
                .team(team)
                .build());
    }

    /**
     * 시즌 스탯 추가
     */
    public void addSeasonStats(SeasonStats stats) {
        this.seasonStats.add(stats);
    }

    /**
     * 은퇴 처리
     */
    public void retire() {
        this.isActive = false;
    }

    /**
     * 복귀 처리
     */
    public void comeback() {
        this.isActive = true;
    }

    /**
     * 우승 경력 여부
     */
    public boolean hasChampionship() {
        return !this.championships.isEmpty();
    }

    /**
     * 특정 대회 우승 여부
     */
    public boolean hasChampionship(String tournament, int year) {
        return this.championships.stream()
                .anyMatch(c -> c.getTournament().equals(tournament) && c.getYear() == year);
    }
}
