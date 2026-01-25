package com.loga.domain.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.loga.domain.report.dto.BugReportResponse;
import com.loga.domain.report.dto.CreateBugReportRequest;
import com.loga.domain.report.entity.BugReport;
import com.loga.domain.report.repository.BugReportRepository;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.PageResponse;

@ExtendWith(MockitoExtension.class)
class BugReportServiceTest {

    @InjectMocks
    private BugReportService bugReportService;

    @Mock
    private BugReportRepository bugReportRepository;

    @Test
    @DisplayName("버그 리포트 생성 성공 - 로그인 사용자")
    void createReport_Success_Auth() {
        // given
        User user = User.builder().email("user@example.com").build();
        user.setId("user1");

        CreateBugReportRequest request = new CreateBugReportRequest();
        request.setTitle("Bug Found");
        request.setDescription("Bug Description");
        request.setType(BugReport.ReportType.BUG);

        BugReport savedReport = BugReport.create("user1", "user@example.com", "Bug Found", "Bug Description",
                BugReport.ReportType.BUG, null);
        given(bugReportRepository.save(any(BugReport.class))).willReturn(savedReport);

        // when
        BugReportResponse result = bugReportService.createReport(request, user);

        // then
        assertThat(result.getTitle()).isEqualTo("Bug Found");
        assertThat(result.getUserEmail()).isEqualTo("user@example.com");
        verify(bugReportRepository).save(any(BugReport.class));
    }

    @Test
    @DisplayName("버그 리포트 생성 성공 - 비로그인")
    void createReport_Success_NoAuth() {
        // given
        CreateBugReportRequest request = new CreateBugReportRequest();
        request.setEmail("guest@example.com");
        request.setTitle("Guest Bug");
        request.setDescription("Desc");
        request.setType(BugReport.ReportType.SUGGESTION);

        BugReport savedReport = BugReport.create(null, "guest@example.com", "Guest Bug", "Desc",
                BugReport.ReportType.SUGGESTION, null);
        given(bugReportRepository.save(any(BugReport.class))).willReturn(savedReport);

        // when
        BugReportResponse result = bugReportService.createReport(request, null);

        // then
        assertThat(result.getTitle()).isEqualTo("Guest Bug");
        assertThat(result.getUserEmail()).isEqualTo("guest@example.com");
        verify(bugReportRepository).save(any(BugReport.class));
    }

    @Test
    @DisplayName("내 리포트 목록 조회 성공")
    void getMyReports_Success() {
        // given
        String userId = "user1";
        Pageable pageable = PageRequest.of(0, 10);
        BugReport report = BugReport.create(userId, "email", "Title", "Desc", BugReport.ReportType.BUG, null);
        Page<BugReport> page = new PageImpl<>(List.of(report));

        given(bugReportRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)).willReturn(page);

        // when
        PageResponse<BugReportResponse> result = bugReportService.getMyReports(userId, pageable);

        // then
        assertThat(result.getPagination().getTotalItems()).isEqualTo(1);
        assertThat(result.getItems().get(0).getTitle()).isEqualTo("Title");
    }

    @Test
    @DisplayName("리포트 상태 변경 성공 (관리자)")
    void updateReportStatus_Success() {
        // given
        String reportId = "report1";
        BugReport report = BugReport.create("user1", "email", "Title", "Desc", BugReport.ReportType.BUG, null);

        given(bugReportRepository.findById(reportId)).willReturn(Optional.of(report));
        given(bugReportRepository.save(any(BugReport.class))).willReturn(report);

        // when
        BugReportResponse result = bugReportService.updateReportStatus(reportId, BugReport.Status.RESOLVED, "Fixed");

        // then
        assertThat(result.getStatus()).isEqualTo(BugReport.Status.RESOLVED.name());
        assertThat(result.getAdminNote()).isEqualTo("Fixed");
        verify(bugReportRepository).save(report);
    }
}
