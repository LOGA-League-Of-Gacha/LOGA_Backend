package com.loga.domain.report.controller;

import com.loga.domain.report.dto.BugReportResponse;
import com.loga.domain.report.dto.CreateBugReportRequest;
import com.loga.domain.report.entity.BugReport;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Bug Report", description = "버그 제보 및 건의사항 API")
public interface BugReportApi {

    @Operation(summary = "버그/건의 제보하기", description = "새로운 버그 리포트나 건의사항을 작성합니다. (로그인 사용자: 자동 기록, 비로그인: 이메일 필수)")
    ResponseEntity<ApiResponse<BugReportResponse>> createReport(
            @RequestBody CreateBugReportRequest request,
            @Parameter(hidden = true) User user);

    @Operation(summary = "내 제보 목록 조회", description = "내가 작성한 제보 내역을 페이징하여 조회합니다.")
    ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getMyReports(
            @Parameter(hidden = true) User user,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "전체 제보 목록 조회 (관리자)", description = "[관리자 전용] 모든 제보 내역을 조회합니다. 상태별(PENDING, RESOLVED 등) 필터링이 가능합니다.")
    ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getAllReports(
            @Parameter(description = "상태 필터 (PENDING, IN_PROGRESS, RESOLVED, REJECTED)") @RequestParam(required = false) BugReport.Status status,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "제보 상태 변경 (관리자)", description = "[관리자 전용] 제보의 상태를 변경하고 관리자 메모를 남깁니다.")
    ResponseEntity<ApiResponse<BugReportResponse>> updateReportStatus(
            @Parameter(description = "리포트 ID") @PathVariable String id,
            @Parameter(description = "변경할 상태") @RequestParam BugReport.Status status,
            @Parameter(description = "관리자 답변/메모") @RequestParam(required = false) String adminNote);
}
