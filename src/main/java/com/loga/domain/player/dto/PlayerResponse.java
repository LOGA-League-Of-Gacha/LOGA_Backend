package com.loga.domain.player.dto;

import com.loga.domain.player.entity.Player;
import lombok.Builder;
import lombok.Getter;

/**
 * 선수 응답 DTO
 * Season Card Model
 */
@Getter
@Builder
public class PlayerResponse {
        private String id;
        private String name;
        private String realName;
        private String position;
        private int year;
        private String teamShort;
        private String teamFull;
        private String teamColor;
        private String region;
        private String nationality;
        private String iso;
        private boolean isWinner;
        private String championshipLeague;
        private Integer championshipYear;
        private String profileImage;
        private int pickedCount;
        private boolean isActive;

        public static PlayerResponse from(Player player) {
                return PlayerResponse.builder()
                                .id(player.getId())
                                .name(player.getName())
                                .realName(player.getRealName())
                                .position(player.getPosition().name())
                                .year(player.getYear())
                                .teamShort(player.getTeamShort())
                                .teamFull(player.getTeamFull())
                                .teamColor(player.getTeamColor())
                                .region(player.getRegion())
                                .nationality(player.getNationality())
                                .iso(player.getIso())
                                .isWinner(player.isChampionshipMember())
                                .championshipLeague(player.getChampionshipLeague())
                                .championshipYear(player.getChampionshipYear())
                                .profileImage(player.getProfileImage())
                                .pickedCount(player.getPickedCount())
                                .isActive(player.isActive())
                                .build();
        }
}
