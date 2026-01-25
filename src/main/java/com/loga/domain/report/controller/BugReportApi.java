package com.loga.domain.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.loga.domain.report.dto.BugReportResponse;
import com.loga.domain.report.dto.CreateBugReportRequest;
import com.loga.domain.report.entity.BugReport;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.ApiResponse;
import com.loga.global.common.dto.response.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 버그 리포트 API 인터페이스 - Swagger 문서화
 */
@Tag(name = "Bug Report", description = "버그 제보 및 건의사항 API - 사용자 피드백 수집")
public interface BugReportApi {

    @Operation(summary = "버그/건의 제보하기", description = """
            새로운 버그 리포트나 건의사항을 작성합니다.

            **제보 유형 (type):**
            - BUG: 버그 제보
            - SUGGESTION: 기능 건의
            - DATA_ERROR: 데이터 오류 (선수 정보 등)
            - OTHER: 기타

            **인증:** 로그인 사용자는 자동 기록, 비로그인 시 email 필수
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제보 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "code": "SUCCESS",
                      "data": {
                        "id": "report123abc",
                        "userId": "user123",
                        "userEmail": "user@gmail.com",
                        "title": "선수 정보 오류",
                        "description": "Faker 선수의 2013년 우승 정보가 누락되어 있습니다.",
                        "type": "DATA_ERROR",
                        "status": "PENDING",
                        "createdAt": "2026-01-25T14:30:00"
                      },
                      "timestamp": "2026-01-25T14:30:00"
                    }
                    """))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    ResponseEntity<ApiResponse<BugReportResponse>> createReport(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "버그 리포트 작성 요청", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBugReportRequest.class), examples = @ExampleObject(value = """
                    {
                      "email": "user@gmail.com",
                      "title": "선수 정보 오류",
                      "description": "Faker 선수의 2013년 우승 정보가 누락되어 있습니다.",
                      "type": "DATA_ERROR",
                      "screenshotUrl": "https://imgur.com/screenshot.png"
                    }
                    """))) @RequestBody CreateBugReportRequest request,
            @Parameter(hidden = true) User user);

    @Operation(summary = "내 제보 목록 조회", description = "내가 작성한 제보 내역을 페이징하여 조회합니다. 최신순 정렬.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요")
    })
    ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getMyReports(
            @Parameter(hidden = true) User user,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "전체 제보 목록 조회 (관리자)", description = """
            [관리자 전용] 모든 제보 내역을 조회합니다.

            **상태 필터:** PENDING, IN_PROGRESS, RESOLVED, REJECTED
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    ResponseEntity<ApiResponse<PageResponse<BugReportResponse>>> getAllReports(
            @Parameter(description = "상태 필터 (PENDING, IN_PROGRESS, RESOLVED, REJECTED)", example = "PENDING") @RequestParam(required = false) BugReport.Status status,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20") @RequestParam(defaultValue = "20") int size);

    @Operation(summary = "제보 상태 변경 (관리자)", description = "[관리자 전용] 제보의 상태를 변경하고 관리자 메모를 남깁니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리포트를 찾을 수 없음")
    })
    ResponseEntity<ApiResponse<BugReportResponse>> updateReportStatus(
            @Parameter(description = "리포트 ID", example = "report123") @PathVariable String id,
            @Parameter(description = "변경할 상태", example = "RESOLVED") @RequestParam BugReport.Status status,
            @Parameter(description = "관리자 답변/메모", example = "수정 완료했습니다.") @RequestParam(required = false) String adminNote);
}
