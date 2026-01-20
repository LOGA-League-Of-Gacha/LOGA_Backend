package com.loga.domain.report.repository;

import com.loga.domain.report.entity.BugReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 버그 리포트 레포지토리
 */
@Repository
public interface BugReportRepository extends MongoRepository<BugReport, String> {

    Page<BugReport> findByStatusOrderByCreatedAtDesc(BugReport.Status status, Pageable pageable);

    Page<BugReport> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<BugReport> findByType(BugReport.ReportType type);

    long countByStatus(BugReport.Status status);
}
