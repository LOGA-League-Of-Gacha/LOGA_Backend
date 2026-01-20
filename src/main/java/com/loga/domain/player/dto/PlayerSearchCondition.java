package com.loga.domain.player.dto;

import com.loga.domain.player.entity.Player;
import lombok.Builder;
import lombok.Getter;

/**
 * 선수 검색 조건 DTO
 * Specification 패턴 적용
 */
@Getter
@Builder
public class PlayerSearchCondition {
    private String name;
    private Player.Position position;
    private String region;
    private String team;
    private Boolean isActive;
    private Boolean hasChampionship;
}
