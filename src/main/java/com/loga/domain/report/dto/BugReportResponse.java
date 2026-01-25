package com.loga.domain.report.dto;

import java.time.LocalDateTime;

import com.loga.domain.report.entity.BugReport;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BugReportResponse {
    private String id;
    private String userId;
    private String userEmail;
    private String title;
    private String description;
    private String type;
    private String screenshotUrl;
    private String status;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public static BugReportResponse from(BugReport report) {
        return BugReportResponse.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .userEmail(report.getUserEmail())
                .title(report.getTitle())
                .description(report.getDescription())
                .type(report.getType().name())
                .screenshotUrl(report.getScreenshotUrl())
                .status(report.getStatus().name())
                .adminNote(report.getAdminNote())
                .createdAt(report.getCreatedAt())
                .resolvedAt(report.getResolvedAt())
                .build();
    }
}
