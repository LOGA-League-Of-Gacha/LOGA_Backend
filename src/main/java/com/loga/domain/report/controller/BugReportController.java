package com.loga.domain.report.controller;

import com.loga.domain.report.dto.BugReportResponse;
import com.loga.domain.report.dto.CreateBugReportRequest;
import com.loga.domain.report.entity.BugReport;
import com.loga.domain.report.service.BugReportService;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.request.PageRequest;
import com.loga.global.common.dto.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class BugReportController {

    private final BugReportService bugReportService;

    @PostMapping
    public ResponseEntity<ApiResponse<BugReportResponse>> createReport(
            @Valid @RequestBody CreateBugReportRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(bugReportService.createReport(request, user)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getMyReports(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                bugReportService.getMyReports(user.getId(), PageRequest.of(page, size).toPageable())));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getAllReports(
            @RequestParam(required = false) BugReport.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                bugReportService.getAllReports(status, PageRequest.of(page, size).toPageable())));
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BugReportResponse>> updateReportStatus(
            @PathVariable String id,
            @RequestParam BugReport.Status status,
            @RequestParam(required = false) String adminNote) {
        return ResponseEntity.ok(ApiResponse.success(
                bugReportService.updateReportStatus(id, status, adminNote)));
    }
}
