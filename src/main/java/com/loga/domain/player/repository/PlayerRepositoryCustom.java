package com.loga.domain.player.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.loga.domain.player.dto.PlayerSearchCondition;
import com.loga.domain.player.entity.Player;

/**
 * 선수 커스텀 레포지토리 인터페이스 Specification 패턴 적용
 */
public interface PlayerRepositoryCustom {

    /**
     * 동적 검색 조건으로 선수 목록 조회
     */
    Page<Player> search(PlayerSearchCondition condition, Pageable pageable);

    /**
     * 포지션별 상위 N명 조회
     */
    List<Player> findTopByPositionOrderByPickedCount(Player.Position position, int limit);
}
