package com.loga.domain.roster.dto;

import jakarta.validation.constraints.NotBlank;

import com.loga.domain.roster.entity.Roster;

import lombok.Getter;
import lombok.Setter;

/**
 * 로스터 생성 요청 DTO
 */
@Getter
@Setter
public class CreateRosterRequest {

    @NotBlank(message = "Top player is required")
    private String topPlayerId;

    @NotBlank(message = "Jungle player is required")
    private String junglePlayerId;

    @NotBlank(message = "Mid player is required")
    private String midPlayerId;

    @NotBlank(message = "ADC player is required")
    private String adcPlayerId;

    @NotBlank(message = "Support player is required")
    private String supportPlayerId;

    private boolean isPublic = false;

    private Roster.GameMode gameMode = Roster.GameMode.NORMAL;
}
