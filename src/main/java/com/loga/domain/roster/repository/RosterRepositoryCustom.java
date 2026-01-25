package com.loga.domain.roster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.loga.domain.roster.dto.RosterSearchCondition;
import com.loga.domain.roster.entity.Roster;

/**
 * 로스터 커스텀 레포지토리 인터페이스
 */
public interface RosterRepositoryCustom {

    /**
     * 동적 검색 조건으로 로스터 목록 조회
     */
    Page<Roster> search(RosterSearchCondition condition, Pageable pageable);

    /**
     * 인기순 정렬 조회
     */
    Page<Roster> findByPopularity(Pageable pageable);

    /**
     * 티어별 랭크 로스터 조회
     */
    Page<Roster> findRankedByTier(String tier, Pageable pageable);
}
