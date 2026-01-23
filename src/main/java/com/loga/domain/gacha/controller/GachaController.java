package com.loga.domain.gacha.controller;

import com.loga.domain.gacha.dto.GachaResultResponse;
import com.loga.domain.gacha.service.GachaService;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 가챠 API 컨트롤러
 */
@RestController
@RequestMapping("/api/gacha")
@RequiredArgsConstructor
public class GachaController implements GachaApi {

    private final GachaService gachaService;

    @Override
    @PostMapping("/draw/{position}")
    public ResponseEntity<ApiResponse<GachaResultResponse>> drawByPosition(
            @PathVariable String position,
            @AuthenticationPrincipal User user) {
        String userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(gachaService.drawByPosition(position, userId)));
    }

    @Override
    @PostMapping("/draw/full")
    public ResponseEntity<ApiResponse<GachaResultResponse>> drawFullRoster(
            @AuthenticationPrincipal User user) {
        String userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(gachaService.drawFullRoster(userId)));
    }

    @Override
    @PostMapping("/reroll/{position}")
    public ResponseEntity<ApiResponse<GachaResultResponse>> reroll(
            @PathVariable String position,
            @AuthenticationPrincipal User user) {
        String userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(gachaService.reroll(position, userId)));
    }
}
