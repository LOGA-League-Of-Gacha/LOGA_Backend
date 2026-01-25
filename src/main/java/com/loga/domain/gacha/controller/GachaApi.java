package com.loga.domain.gacha.controller;

import com.loga.domain.gacha.dto.GachaResultResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Gacha", description = "가챠(뽑기) 시스템 API")
public interface GachaApi {

    @Operation(summary = "단일 선수 뽑기", description = "지정된 포지션(TOP, JUNGLE, MID, ADC, SUPPORT)의 선수 1명을 랜덤하게 뽑습니다.")
    ResponseEntity<ApiResponse<GachaResultResponse>> drawByPosition(
            @Parameter(description = "포지션") @PathVariable String position,
            @Parameter(hidden = true) User user);

    @Operation(summary = "전체 로스터 뽑기", description = "5개 포지션(TOP~SUPPORT) 모두를 한 번에 뽑습니다. (5명 신규 생성)")
    ResponseEntity<ApiResponse<GachaResultResponse>> drawFullRoster(
            @Parameter(hidden = true) User user);

    @Operation(summary = "리롤 (Reroll)", description = "특정 포지션의 선수를 다시 뽑습니다. (멤버십에 따라 횟수 제한)")
    ResponseEntity<ApiResponse<GachaResultResponse>> reroll(
            @Parameter(description = "리롤할 포지션") @PathVariable String position,
            @Parameter(hidden = true) User user);
}
