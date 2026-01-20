package com.loga.domain.player.controller;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.player.dto.PlayerSearchCondition;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.service.PlayerService;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.request.PageRequest;
import com.loga.global.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 선수 API 컨트롤러
 */
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /**
     * 전체 선수 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getAllPlayers() {
        return ResponseEntity.ok(ApiResponse.success(playerService.getAllPlayers()));
    }

    /**
     * 선수 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlayerResponse>> getPlayer(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(playerService.getPlayerById(id)));
    }

    /**
     * 선수 검색 (Specification 패턴)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<PlayerResponse>>> searchPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String team,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean hasChampionship,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PlayerSearchCondition condition = PlayerSearchCondition.builder()
                .name(name)
                .position(position != null ? Player.Position.valueOf(position.toUpperCase()) : null)
                .region(region)
                .team(team)
                .isActive(isActive)
                .hasChampionship(hasChampionship)
                .build();

        return ResponseEntity.ok(ApiResponse.success(
                playerService.searchPlayers(condition, PageRequest.of(page, size).toPageable())));
    }

    /**
     * 포지션별 선수 목록
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByPosition(
            @PathVariable String position) {
        Player.Position pos = Player.Position.valueOf(position.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success(playerService.getPlayersByPosition(pos)));
    }

    /**
     * 리전별 선수 목록
     */
    @GetMapping("/region/{region}")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByRegion(
            @PathVariable String region) {
        return ResponseEntity.ok(ApiResponse.success(playerService.getPlayersByRegion(region)));
    }

    /**
     * 팀별 선수 목록
     */
    @GetMapping("/team/{team}")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getPlayersByTeam(
            @PathVariable String team) {
        return ResponseEntity.ok(ApiResponse.success(playerService.getPlayersByTeam(team)));
    }

    /**
     * 가장 많이 뽑힌 선수 TOP 10
     */
    @GetMapping("/top-picked")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getTopPickedPlayers() {
        return ResponseEntity.ok(ApiResponse.success(playerService.getTopPickedPlayers()));
    }

    /**
     * 현역 선수 목록
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> getActivePlayers() {
        return ResponseEntity.ok(ApiResponse.success(playerService.getActivePlayers()));
    }
}
