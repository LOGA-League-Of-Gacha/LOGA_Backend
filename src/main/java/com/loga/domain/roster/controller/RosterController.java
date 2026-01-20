package com.loga.domain.roster.controller;

import com.loga.domain.roster.dto.CreateRosterRequest;
import com.loga.domain.roster.dto.RosterResponse;
import com.loga.domain.roster.dto.RosterSearchCondition;
import com.loga.domain.roster.entity.Roster;
import com.loga.domain.roster.service.RosterService;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.request.PageRequest;
import com.loga.global.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 로스터 API 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    /**
     * 로스터 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RosterResponse>> createRoster(
            @Valid @RequestBody CreateRosterRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(rosterService.createRoster(request, user)));
    }

    /**
     * 로스터 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RosterResponse>> getRoster(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(rosterService.getRosterById(id)));
    }

    /**
     * 내 로스터 목록
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getMyRosters(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                rosterService.getUserRosters(user.getId(), PageRequest.of(page, size).toPageable())));
    }

    /**
     * 유저 로스터 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getUserRosters(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                rosterService.getUserRosters(userId, PageRequest.of(page, size).toPageable())));
    }

    /**
     * 로스터 검색
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> searchRosters(
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) Boolean isChampionship,
            @RequestParam(required = false) String gameMode,
            @RequestParam(required = false) String tier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        RosterSearchCondition condition = RosterSearchCondition.builder()
                .isPublic(isPublic)
                .isChampionship(isChampionship)
                .gameMode(gameMode != null ? Roster.GameMode.valueOf(gameMode.toUpperCase()) : null)
                .tier(tier)
                .build();

        return ResponseEntity.ok(ApiResponse.success(
                rosterService.searchRosters(condition, PageRequest.of(page, size).toPageable())));
    }

    /**
     * 좋아요 토글
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<RosterResponse>> toggleLike(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(rosterService.toggleLike(id, user)));
    }

    /**
     * 로스터 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoster(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        rosterService.deleteRoster(id, user);
        return ResponseEntity.ok(ApiResponse.success("Roster deleted successfully"));
    }
}
