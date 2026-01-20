package com.loga.domain.report.service;

import com.loga.domain.report.dto.BugReportResponse;
import com.loga.domain.report.dto.CreateBugReportRequest;
import com.loga.domain.report.entity.BugReport;
import com.loga.domain.report.repository.BugReportRepository;
import com.loga.domain.user.entity.User;
import com.loga.global.common.dto.response.PageResponse;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;

    @Transactional
    public BugReportResponse createReport(CreateBugReportRequest request, User user) {
        BugReport report = BugReport.create(
                user != null ? user.getId() : null,
                user != null ? user.getEmail() : request.getEmail(),
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getScreenshotUrl()
        );
        report = bugReportRepository.save(report);
        log.info("Bug report created: {} - {}", report.getId(), report.getTitle());
        return BugReportResponse.from(report);
    }

    @Transactional(readOnly = true)
    public PageResponse<BugReportResponse> getMyReports(String userId, Pageable pageable) {
        Page<BugReport> page = bugReportRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return PageResponse.from(page, BugReportResponse::from);
    }

    @Transactional(readOnly = true)
    public PageResponse<BugReportResponse> getAllReports(BugReport.Status status, Pageable pageable) {
        Page<BugReport> page = status != null
                ? bugReportRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                : bugReportRepository.findAll(pageable);
        return PageResponse.from(page, BugReportResponse::from);
    }

    @Transactional
    public BugReportResponse updateReportStatus(String reportId, BugReport.Status status, String adminNote) {
        BugReport report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        switch (status) {
            case IN_PROGRESS -> report.startProgress(adminNote);
            case RESOLVED -> report.resolve(adminNote);
            case REJECTED -> report.reject(adminNote);
            default -> {}
        }

        bugReportRepository.save(report);
        return BugReportResponse.from(report);
    }
}
