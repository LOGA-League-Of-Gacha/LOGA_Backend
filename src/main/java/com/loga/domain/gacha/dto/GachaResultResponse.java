package com.loga.domain.gacha.dto;

import com.loga.domain.player.dto.PlayerResponse;

import lombok.Builder;
import lombok.Getter;

/**
 * 가챠 결과 응답 DTO
 */
@Getter
@Builder
public class GachaResultResponse {
    // 단일 뽑기 결과
    private PlayerResponse player;

    // 전체 뽑기 결과 (5명)
    private PlayerResponse top;
    private PlayerResponse jungle;
    private PlayerResponse mid;
    private PlayerResponse adc;
    private PlayerResponse support;

    // 우승 로스터 매칭 정보
    private boolean isChampionshipRoster;
    private String matchedChampionship;
    private Integer matchedYear;

    /**
     * 단일 뽑기 결과 생성
     */
    public static GachaResultResponse ofSingle(PlayerResponse player) {
        return GachaResultResponse.builder()
                .player(player)
                .isChampionshipRoster(false)
                .build();
    }

    /**
     * 전체 뽑기 결과 생성
     */
    public static GachaResultResponse ofFullRoster(
            PlayerResponse top, PlayerResponse jungle, PlayerResponse mid,
            PlayerResponse adc, PlayerResponse support,
            boolean isChampionship, String matchedChampionship, Integer matchedYear) {
        return GachaResultResponse.builder()
                .top(top)
                .jungle(jungle)
                .mid(mid)
                .adc(adc)
                .support(support)
                .isChampionshipRoster(isChampionship)
                .matchedChampionship(matchedChampionship)
                .matchedYear(matchedYear)
                .build();
    }
}
