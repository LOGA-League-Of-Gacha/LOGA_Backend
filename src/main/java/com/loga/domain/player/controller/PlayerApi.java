package com.loga.domain.player.controller;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Player", description = "선수 도감 및 검색 API")
public interface PlayerApi {

    @Operation(summary = "전체 선수 목록 조회", description = "등록된 모든 선수의 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getAllPlayers();

    @Operation(summary = "선수 상세 조회", description = "선수 ID(e.g., 'faker_2013')로 특정 선수의 상세 정보를 조회합니다.")
    ResponseEntity<ApiResponse<PlayerResponse>> getPlayer(
            @Parameter(description = "선수 ID") @PathVariable String id);

    @Operation(summary = "선수 검색", description = "다양한 조건(이름, 포지션, 지역, 팀 등)으로 선수를 검색합니다.")
    ResponseEntity<ApiResponse<PageResponse<PlayerResponse>>> searchPlayers(
            @Parameter(description = "선수 이름 (부분 일치)") @RequestParam(required = false) String name,
            @Parameter(description = "포지션 (TOP, JUNGLE, MID, ADC, SUPPORT)") @RequestParam(required = false) String position,
            @Parameter(description = "지역 (LCK, LPL, etc.)") @RequestParam(required = false) String region,
            @Parameter(description = "팀 약어 (e.g. SKT, GEN)") @RequestParam(required = false) String team,
            @Parameter(description = "현역 여부") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "우승 경력 보유 여부") @RequestParam(required = false) Boolean hasChampionship,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "포지션별 선수 조회", description = "특정 포지션의 모든 선수를 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByPosition(
            @Parameter(description = "포지션") @PathVariable String position);

    @Operation(summary = "지역별 선수 조회", description = "특정 지역(리그)의 모든 선수를 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByRegion(
            @Parameter(description = "지역 (LCK, LPL...)") @PathVariable String region);

    @Operation(summary = "팀별 선수 조회", description = "특정 팀(약어)의 모든 선수를 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByTeam(
            @Parameter(description = "팀 약어 (SKT)") @PathVariable String team);

    @Operation(summary = "인기 선수 TOP 10", description = "가챠에서 가장 많이 뽑힌 인기 선수 10명을 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getTopPickedPlayers();

    @Operation(summary = "현역 선수 조회", description = "현재 활동 중인(Active) 선수 목록을 조회합니다.")
    ResponseEntity<ApiResponse<List<PlayerResponse>>> getActivePlayers();
}
