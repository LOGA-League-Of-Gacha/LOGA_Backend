package com.loga.domain.player.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.loga.infrastructure.persistence.BaseDocument;

import lombok.*;

/**
 * 선수 도메인 엔티티 (Season Card Model) 각 문서는 특정 시즌의 선수 카드를 나타냄
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

    @Indexed
    private Position position;

    private int year;

    @Indexed
    private String teamShort;

    private String teamFull;

    private String teamColor;

    @Indexed
    private String region;

    private String nationality;

    private String iso;

    private boolean isWinner;

    private String championshipLeague;

    private Integer championshipYear;

    @Builder.Default
    private int pickedCount = 0;

    private String profileImage;

    @Builder.Default
    private boolean isActive = true;

    public enum Position {
        TOP, JUNGLE, MID, ADC, SUPPORT
    }

    // ===== Factory Methods =====

    public static Player create(String id, String name, String realName, Position position, int year,
            String teamShort, String teamFull, String teamColor,
            String region, String nationality, String iso) {
        Player player = Player.builder()
                .name(name)
                .realName(realName)
                .position(position)
                .year(year)
                .teamShort(teamShort)
                .teamFull(teamFull)
                .teamColor(teamColor)
                .region(region)
                .nationality(nationality)
                .iso(iso)
                .isActive(true)
                .build();
        player.setId(id);
        return player;
    }

    // ===== Domain Logic =====

    /**
     * 가챠에서 뽑힘 (카운트 증가)
     */
    public void picked() {
        this.pickedCount++;
    }

    /**
     * 우승 멤버 여부 확인
     */
    public boolean isChampionshipMember() {
        return isWinner;
    }
}
