package com.loga.domain.player.service;

import com.loga.domain.player.dto.PlayerResponse;
import com.loga.domain.player.dto.PlayerSearchCondition;
import com.loga.domain.player.entity.Player;
import com.loga.domain.player.repository.PlayerRepository;
import com.loga.global.common.dto.response.PageResponse;
import com.loga.global.error.BusinessException;
import com.loga.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 선수 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * 전체 선수 목록
     */
    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 선수 상세 조회
     */
    public PlayerResponse getPlayerById(String id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PLAYER_NOT_FOUND));
        return PlayerResponse.from(player);
    }

    /**
     * 동적 검색 조건으로 선수 검색 (Specification 패턴)
     */
    public PageResponse<PlayerResponse> searchPlayers(PlayerSearchCondition condition, Pageable pageable) {
        Page<Player> page = playerRepository.search(condition, pageable);
        return PageResponse.from(page, PlayerResponse::from);
    }

    /**
     * 포지션별 선수 목록
     */
    public List<PlayerResponse> getPlayersByPosition(Player.Position position) {
        return playerRepository.findByPosition(position).stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 리전별 선수 목록
     */
    public List<PlayerResponse> getPlayersByRegion(String region) {
        return playerRepository.findByRegion(region).stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 팀별 선수 목록
     */
    public List<PlayerResponse> getPlayersByTeam(String team) {
        return playerRepository.findByCurrentTeam(team).stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 선수 이름 검색
     */
    public List<PlayerResponse> searchByName(String query) {
        return playerRepository.findByNameContainingIgnoreCase(query).stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 가장 많이 뽑힌 선수 TOP 10
     */
    public List<PlayerResponse> getTopPickedPlayers() {
        return playerRepository.findTop10ByOrderByPickedCountDesc().stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 현역 선수 목록
     */
    public List<PlayerResponse> getActivePlayers() {
        return playerRepository.findByIsActiveTrue().stream()
                .map(PlayerResponse::from)
                .toList();
    }

    /**
     * 포지션별 상위 선수
     */
    public List<PlayerResponse> getTopPlayersByPosition(Player.Position position, int limit) {
        return playerRepository.findTopByPositionOrderByPickedCount(position, limit).stream()
                .map(PlayerResponse::from)
                .toList();
    }
}
