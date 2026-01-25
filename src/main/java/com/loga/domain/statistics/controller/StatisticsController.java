package com.loga.domain.statistics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.statistics.dto.StatisticsResponse;
import com.loga.domain.statistics.service.StatisticsService;
import com.loga.global.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController implements StatisticsApi {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<ApiResponse<StatisticsResponse>> getOverallStatistics() {
        return ResponseEntity.ok(ApiResponse.success(statisticsService.getOverallStatistics()));
    }

    @GetMapping("/top-picked")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getTopPickedPlayers() {
        return ResponseEntity.ok(ApiResponse.success(statisticsService.getTopPickedPlayers()));
    }

    @GetMapping("/top-by-position")
    public ResponseEntity<ApiResponse<Map<String, PlayerResponse>>> getTopPlayerByPosition() {
        return ResponseEntity.ok(ApiResponse.success(statisticsService.getTopPlayerByPosition()));
    }
}
