package com.loga.domain.roster.dto;

import com.loga.domain.roster.entity.Roster;

import lombok.Builder;
import lombok.Getter;

/**
 * 로스터 검색 조건 DTO
 */
@Getter
@Builder
public class RosterSearchCondition {

    private String userId;
    private Boolean isPublic;
    private Boolean isChampionship;
    private Roster.GameMode gameMode;
    private String tier;
}
