package com.loga.domain.community.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.loga.domain.community.dto.CommentResponse;
import com.loga.domain.community.dto.CreateCommentRequest;
import com.loga.domain.community.service.CommunityService;
import com.loga.domain.roster.dto.RosterResponse;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.request.PageRequest;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController implements CommunityApi {

    private final CommunityService communityService;

    @GetMapping("/rosters")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getCommunityRosters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getCommunityRosters(PageRequest.of(page, size).toPageable())));
    }

    @GetMapping("/rosters/popular")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getPopularRosters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getPopularRosters(PageRequest.of(page, size).toPageable())));
    }

    @GetMapping("/rosters/championships")
    public ResponseEntity<ApiResponse<PageResponse<RosterResponse>>> getChampionshipRosters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getChampionshipRosters(PageRequest.of(page, size).toPageable())));
    }

    @GetMapping("/rosters/{rosterId}/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(
            @PathVariable String rosterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.getComments(rosterId, PageRequest.of(page, size).toPageable())));
    }

    @PostMapping("/rosters/{rosterId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable String rosterId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(
                communityService.createComment(rosterId, request, user)));
    }

    @DeleteMapping("/rosters/{rosterId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable String rosterId,
            @PathVariable String commentId,
            @AuthenticationPrincipal User user) {
        communityService.deleteComment(rosterId, commentId, user);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted"));
    }
}
