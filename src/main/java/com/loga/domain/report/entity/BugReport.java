package com.loga.domain.report.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.loga.infrastructure.persistence.BaseDocument;

import lombok.*;

/**
 * 버그 리포트 도메인 엔티티
 */
@Document(collection = "bug_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BugReport extends BaseDocument {

    private String userId;

    private String userEmail;

    private String title;

    private String description;

    private ReportType type;

    private String screenshotUrl;

    @Builder.Default
    private Status status = Status.PENDING;

    private String adminNote;

    private LocalDateTime resolvedAt;

    public enum ReportType {
        BUG, // 버그 제보
        DATA_ERROR, // 선수 데이터 오류
        SUGGESTION, // 기능 제안
        OTHER
    }

    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED, REJECTED
    }

    // ===== Factory Methods =====

    public static BugReport create(String userId, String userEmail, String title,
            String description, ReportType type, String screenshotUrl) {
        return BugReport.builder()
                .userId(userId)
                .userEmail(userEmail)
                .title(title)
                .description(description)
                .type(type)
                .screenshotUrl(screenshotUrl)
                .status(Status.PENDING)
                .build();
    }

    // ===== Domain Logic =====

    public void startProgress(String adminNote) {
        this.status = Status.IN_PROGRESS;
        this.adminNote = adminNote;
    }

    public void resolve(String adminNote) {
        this.status = Status.RESOLVED;
        this.adminNote = adminNote;
        this.resolvedAt = LocalDateTime.now();
    }

    public void reject(String adminNote) {
        this.status = Status.REJECTED;
        this.adminNote = adminNote;
        this.resolvedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return this.status == Status.PENDING;
    }

    public boolean isResolved() {
        return this.status == Status.RESOLVED;
    }
}
