package com.loga.domain.player.entity;

import com.loga.infrastructure.persistence.BaseDocument;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 우승 로스터 엔티티
 * 실제 대회 우승 로스터 정보 저장
 */
@Document(collection = "championships")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Championship extends BaseDocument {

    private String tournament; // Worlds, MSI

    private int year;

    private String team;

    private String region;

    // 우승 로스터 선수 ID들
    private RosterPlayers players;

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

        public boolean matches(String top, String jungle, String mid, String adc, String support) {
            return topPlayerId.equals(top) &&
                   junglePlayerId.equals(jungle) &&
                   midPlayerId.equals(mid) &&
                   adcPlayerId.equals(adc) &&
                   supportPlayerId.equals(support);
        }
    }

    // ===== Factory Methods =====

    public static Championship create(String tournament, int year, String team, String region,
                                       RosterPlayers players) {
        return Championship.builder()
                .tournament(tournament)
                .year(year)
                .team(team)
                .region(region)
                .players(players)
                .build();
    }

    // ===== Domain Logic =====

    public String getDisplayName() {
        return year + " " + tournament + " - " + team;
    }

    public boolean matchesRoster(String top, String jungle, String mid, String adc, String support) {
        return players.matches(top, jungle, mid, adc, support);
    }
}
